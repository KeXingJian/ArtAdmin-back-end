package com.kxj.artadmin.service.impl;

import com.kxj.artadmin.config.UploadConfig;
import com.kxj.artadmin.dao.PictureDao;
import com.kxj.artadmin.dao.VideoDao;
import com.kxj.artadmin.enume.Status;
import com.kxj.artadmin.enume.Type;
import com.kxj.artadmin.model.Immutables;
import com.kxj.artadmin.model.Picture;
import com.kxj.artadmin.model.Video;
import com.kxj.artadmin.model.dto.AdminVideoView;
import com.kxj.artadmin.model.dto.EditVideoInput;
import com.kxj.artadmin.model.dto.StatusVideoInput;
import com.kxj.artadmin.model.dto.UserVideoView;
import com.kxj.artadmin.service.VideoService;
import com.kxj.artadmin.utils.FileUtil;
import com.kxj.artadmin.model.PageParam;
import jakarta.annotation.Resource;
import org.babyfish.jimmer.Page;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {

    @Resource
    private VideoDao videoDao;

    @Resource
    private UploadConfig uploadConfig;

    @Resource
    private PictureDao pictureDao;

    //用户页面
    @Cacheable("data")
    @Override
    public Page<UserVideoView> getListForUser(PageParam pageParam) {
        return videoDao.getList(UserVideoView.class, pageParam);
    }


    //加载视频
    @Override
    public void loading() {
        try {
            List<String> uuidList = FileUtil.moveVideoToAudit(uploadConfig.getWorkArea(), uploadConfig.getAuditArea());
            List<Video> videos = new ArrayList<>();
            uuidList.stream().map(VideoServiceImpl::removeFileExtension).forEach(uuid -> {
                Video video = Immutables.createVideo(draft -> {
                    draft.setUuid(uuid);
                    draft.setStatus(Status.WAIT_AUDIT);
                });
                videos.add(video);
            });
            videoDao.addVideos(videos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //更新视频
    @Override
    public void updateVideo(MultipartFile file, EditVideoInput videoInput) {

        if (videoInput.getPicture() != null
                && videoInput.getPicture().getPath() != null
                && file != null && !file.isEmpty()
        ) {
            //有图片有path更换图片
            String newPath = FileUtil.uploadPicture(file, uploadConfig.getPictureDir()+ Type.COVER.getPath());
            try {
                FileUtil.removePicture
                        (
                                uploadConfig.getPictureDir()+ Type.COVER.getPath()+ "/" + videoInput.getPicture().getPath(),
                                uploadConfig.getPictureRecycle()
                        );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            videoInput.getPicture().setPath(newPath);
        } else if (videoInput.getPicture() != null && file != null && !file.isEmpty()) {
            //没path有图片加载图片
            String newPath = FileUtil.uploadPicture(file, uploadConfig.getPictureDir()+ Type.COVER.getPath());
            videoInput.getPicture().setPath(newPath);
        }

        videoDao.updateVideo(videoInput);
    }

    //状态查询
    @Override
    public Page<AdminVideoView> getByStatus(PageParam pageParam, Status status) {
        return videoDao.getByStatus(pageParam, status);
    }

    //uuid查询
    @Override
    public AdminVideoView getById(String uuid) {
        return videoDao.getById(uuid);
    }

    @Override
    public void changeStatus(StatusVideoInput input) {

        if (input.getStatus() == Status.SCRAP) {

            try {
                //回收视频

                FileUtil.removePicture
                        (
                                uploadConfig.getAuditArea() + input.getUuid() + ".mp4",
                                uploadConfig.getVideoRecycle()
                        );

                //回收封面
                Picture picture = null;
                if (input.getPicture() != null) {
                    picture = pictureDao.getPictureById(input.getPicture().getId());
                }
                if (picture != null && picture.path() != null) {
                    FileUtil.removePicture
                            (

                                    uploadConfig.getPictureDir()+ Type.COVER.getPath()+ "/" + picture.path(),
                                    uploadConfig.getPictureRecycle()
                            );
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        } else if (input.getStatus() == Status.WAIT_PUBLIC && input.getArtist() != null) {

            String newUuid = input.getArtist().getName() + "$" + input.getUuid();

            try {
                FileUtil.moveAuditToPass(uploadConfig.getAuditArea(),
                        uploadConfig.getPassArea(),
                        newUuid + ".mp4");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

        //疑似重复和发布不处理
        videoDao.changeStatus(input);
    }

    @Override
    public UserVideoView searchByUser(String uuid) {
        return videoDao.searchByUser(uuid);
    }

    @Override
    public Page<UserVideoView> searchByLabel(List<Long> label, PageParam pageParam) {
        return videoDao.getByLabel(label,pageParam);
    }

    //去文件后缀
    private static String removeFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }

        // 查找最后一个点号的位置
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return fileName; // 没有后缀名
        }

        // 返回不带后缀的文件名
        return fileName.substring(0, lastDotIndex);
    }


}
