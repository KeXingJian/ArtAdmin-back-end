package com.kxj.artadmin.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.kxj.artadmin.config.UploadConfig;
import com.kxj.artadmin.enume.Status;
import com.kxj.artadmin.enume.Type;
import com.kxj.artadmin.model.dto.*;
import com.kxj.artadmin.result.Result;
import com.kxj.artadmin.enume.ResultCodeEnum;
import com.kxj.artadmin.service.VideoService;
import com.kxj.artadmin.model.PageParam;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.babyfish.jimmer.Page;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/video")
@CrossOrigin
@Slf4j
public class VideoController {

    @Resource
    private VideoService videoService;

    @Resource
    private UploadConfig uploadConfig;

    private final Cache passCache;

    public VideoController(CacheManager cacheManager) {
        this.passCache = cacheManager.getCache("pass");
    }

    @GetMapping("userList/{index}/{size}")
    public Result<Page<UserVideoView>> getListForUser(
            @PathVariable int index,
            @PathVariable int size
    ) {
        PageParam pageParam = new PageParam();
        pageParam.setPageIndex(index - 1);
        pageParam.setPageSize(size);
        Page<UserVideoView> list = videoService.getListForUser(pageParam);
        return Result.success(list);
    }

    //编辑视频
    @PreAuthorize("hasAnyRole('root')")
    @GetMapping("loading")
    public Result<String> getLoading() {
        videoService.loading();
        return Result.success("加载完毕");
    }
    @PreAuthorize("hasAnyRole('root', 'admin')")
    @PutMapping("editVideo")
    public Result<String> editVideo(
            @Nullable @RequestParam("file") MultipartFile file,
            @RequestParam("editVideo") String editVideo
    ) {
        EditVideoInput videoInput = JSONUtil.toBean(editVideo, EditVideoInput.class);
        videoService.updateVideo(file, videoInput);
        return Result.success("更新成功");
    }

    //审核视频列表
    @PreAuthorize("hasAnyRole('root', 'admin')")
    @GetMapping("getByStatus/{index}/{size}/{value}")
    public Result<Page<AdminVideoView>> getVideoByStatus(
            @PathVariable int index,
            @PathVariable int size,
            @PathVariable int value
    ) {
        PageParam pageParam = new PageParam();
        pageParam.setPageIndex(index - 1);
        pageParam.setPageSize(size);
        Status status = null;
        if (value != -1) {
            status = Status.values()[value];
        }
        if (value>=5){
            return Result.failure(ResultCodeEnum.DATA_ERROR.getMessage(),
                    ResultCodeEnum.DATA_ERROR.getCode());
        }
        Page<AdminVideoView> page = videoService.getByStatus(pageParam, status);
        return Result.success(page);
    }

    //admin通uuid查询视频
    @PreAuthorize("hasAnyRole('root', 'admin')")
    @GetMapping("getByUuid/{uuid}")
    public Result<AdminVideoView> getByUuid(
            @PathVariable String uuid
    ) {

        if (StringUtils.isEmpty(uuid)) {
            return Result.failure("uuid不能为空", ResultCodeEnum.Empty.getCode());
        }

        AdminVideoView adminVideoView = videoService.getById(uuid);
        return Result.success(adminVideoView);
    }

    //视频状态转化
    @PreAuthorize("hasAnyRole('root', 'admin')")
    @PutMapping("changeStatus")
    public Result<String> changeStatus(
            @RequestBody StatusVideoInput input

    ) {
        videoService.changeStatus(input);
        return Result.success("转换成功");
    }


    //主页视频
    @GetMapping("searchByUser/{uuid}")
    public Result<UserVideoView> searchByUser(
            @PathVariable String uuid
    ){
        UserVideoView view = videoService.searchByUser(uuid);
        return Result.success(view);
    }

    @PostMapping("searchByLabel/{index}/{size}")
    public Result<Page<UserVideoView>> searchByLabel(
            @RequestBody List<Long> label,
            @PathVariable int index,
            @PathVariable int size
    ){

        PageParam pageParam = new PageParam();
        pageParam.setPageIndex(index-1);
        pageParam.setPageSize(size);
        Page<UserVideoView> page = videoService.searchByLabel(label, pageParam);

        return Result.success(page);
    }


    @GetMapping("image/{value}/{uuid}")
    public ResponseEntity<StreamingResponseBody> getImage(
            @PathVariable String uuid,
            @PathVariable int value
    )  {
        String path = uploadConfig.getPictureDir()+Type.values()[value].getPath()+ "/" + uuid;

        // 根据uuid查找图片路径
        Path filePath = Paths.get(path);

        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        String contentType = getContentType(filePath.getFileName().toString());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + filePath.getFileName().toString());
        headers.add(HttpHeaders.CONTENT_TYPE, contentType);

        StreamingResponseBody body = outputStream -> {
            try (var inputStream = Files.newInputStream(filePath)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                throw new RuntimeException("Error reading file", e);
            }
        };

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(contentType))
                .body(body);
    }

    @GetMapping("playVideo/{uuid}/{pass}")
    public ResponseEntity<StreamingResponseBody> playVideo(
            @PathVariable String uuid,
            @PathVariable String pass,
            @RequestHeader(value = "Range", required = false) String rangeHeader
    ) {
        Path filePath = Paths.get(uploadConfig.getAuditArea() + uuid+".mp4");

        if (!Files.exists(filePath)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (check(uuid,pass)){
            return streamFile(filePath, rangeHeader);
        }
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
    }

    private boolean check(String uuid, String pass) {
        Cache.ValueWrapper valueWrapper = passCache.get(uuid);
        return valueWrapper != null && Objects.equals((String) valueWrapper.get(), pass);
    }

    //获取临时通行证
    @GetMapping("getTempPass/{uuid}")
    public Result<String> getTempPass(
            @PathVariable String uuid
    ) {

        String pass = IdUtil.fastSimpleUUID().substring(0, 8);
        if (passCache.get(uuid) != null ) {
            Cache.ValueWrapper valueWrapper = passCache.get(uuid);
            if (valueWrapper != null) {
                pass = (String) valueWrapper.get();
            }
        }
        passCache.put(uuid, pass);
        return Result.success(pass);
    }


    @GetMapping("playUserVideo/{uuid}/{pass}")
    public ResponseEntity<StreamingResponseBody> playUserVideo(
            @PathVariable String uuid,
            @PathVariable String pass,
            @RequestHeader(value = "Range", required = false) String rangeHeader
    ) {
        Path filePath = Paths.get(uploadConfig.getPassArea() + uuid.replace("$", "/")+".mp4");
        if (!Files.exists(filePath)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        String[] split = uuid.split("\\$");
        if (check(split[1],pass)){
            return streamFile(filePath, rangeHeader);
        }
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
    }

    public static ResponseEntity<StreamingResponseBody> streamFile(
            Path filePath,
            @RequestHeader(value = "Range", required = false) String rangeHeader
    ) {
        try {
            long fileSize = Files.size(filePath);

            if (rangeHeader == null || !rangeHeader.startsWith("bytes=")) {
                return fullFileResponse(filePath, fileSize);
            }

            String[] ranges = rangeHeader.substring(6).split("-");
            long start = Long.parseLong(ranges[0]);
            long end = ranges.length > 1 && !ranges[1].isEmpty() ? Long.parseLong(ranges[1]) : fileSize - 1;

            if (start >= fileSize) {
                return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).build();
            }

            end = Math.min(end, fileSize - 1);
            long contentLength = end - start + 1;

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Range", "bytes " + start + "-" + end + "/" + fileSize);
            headers.add("Accept-Ranges", "bytes");
            headers.add("Content-Length", String.valueOf(contentLength));
            headers.setContentType(MediaType.parseMediaType("video/mp4"));

            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .headers(headers)
                    .body(out -> streamFileRange(filePath, start, contentLength, out));

        } catch (NumberFormatException | IOException e) {
            log.error("Error processing request for file: {}", filePath, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    private static void streamFileRange(Path filePath, long start, long contentLength, OutputStream outputStream) throws IOException {
        try (InputStream inputStream = Files.newInputStream(filePath)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            long bytesRemaining = contentLength;
            long skipped = inputStream.skip(start);

            if (skipped < start) {
                throw new IOException("Failed to skip to requested position in file.");
            }

            while ((bytesRead = inputStream.read(buffer)) > 0 && bytesRemaining > 0) {
                try {
                    outputStream.write(buffer, 0, (int) Math.min(bytesRead, bytesRemaining));
                    bytesRemaining -= bytesRead;
                } catch (IOException e) {
                    break;
                }
            }
        }
    }

    private static ResponseEntity<StreamingResponseBody> fullFileResponse(Path filePath, long fileSize) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Length", String.valueOf(fileSize));
        headers.setContentType(MediaType.parseMediaType("video/mp4"));

        return ResponseEntity.ok()
                .headers(headers)
                .body(out -> {
                    try (InputStream in = Files.newInputStream(filePath)) {
                        byte[] buffer = new byte[8192];
                        int n;
                        while ((n = in.read(buffer)) != -1) {
                            try {
                                out.write(buffer, 0, n);
                            } catch (IOException e) {
                                break;
                            }
                        }
                    } catch (IOException e) {
                        log.error("Failed to stream video content", e);
                    }
                });
    }

    private String getContentType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        return switch (extension) {
            case "jpg", "jpeg" -> MediaType.IMAGE_JPEG_VALUE;
            case "png" -> MediaType.IMAGE_PNG_VALUE;
            case "gif" -> MediaType.IMAGE_GIF_VALUE;
            case "svg" -> "image/svg+xml";
            default -> "application/octet-stream";
        };
    }

}
