package com.kxj.artadmin.controller;


import cn.hutool.json.JSONUtil;
import com.kxj.artadmin.model.dto.*;
import com.kxj.artadmin.result.Result;
import com.kxj.artadmin.enume.ResultCodeEnum;
import com.kxj.artadmin.service.ArtistService;

import com.kxj.artadmin.model.PageParam;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import org.babyfish.jimmer.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/artist")
@CrossOrigin
public class ArtistController {

    @Resource
    private ArtistService artistService;


    @GetMapping("getList/{index}/{size}")
    public Result<Page<ArtistView>> getArtistList(
            @PathVariable int index,
            @PathVariable int size
            ) {
        PageParam pageParam = new PageParam();
        pageParam.setPageIndex(index-1);
        pageParam.setPageSize(size);
        Page<ArtistView> list = artistService.getList(pageParam);
        return Result.success(list);
    }
    @PostMapping("getArtistCollection")
    public Result<Page<AtistCollectionView>> getArtistCollection(
            @RequestBody PageParam pageParam

    ) {
        Page<AtistCollectionView> collections = artistService.getCollection(pageParam);
        return  Result.success(collections);
    }

    @PreAuthorize("hasAnyRole('root', 'admin')")
    @PostMapping("addArtist")
    public Result<String> addArtist(
            @RequestParam("file") MultipartFile file,
            @RequestParam("artistInput") String artistAdd
    ) {

        if (file.isEmpty()) {
            return Result.failure(ResultCodeEnum.Empty.getMessage()
                    , ResultCodeEnum.Empty.getCode());
        }
        ArtistAddInput artistInput = JSONUtil.toBean(artistAdd, ArtistAddInput.class);
        try {

          artistService.addArtist(file,artistInput);

            return Result.success("Add successfully.");
        } catch (Exception e) {
            return Result.failure(ResultCodeEnum.FAIL.getMessage(),
                    ResultCodeEnum.FAIL.getCode());
        }
    }
    @PreAuthorize("hasAnyRole('root', 'admin')")
    @PutMapping("updateArtist")
    public Result<String> updateArtist(
            @Nullable @RequestParam("file") MultipartFile file,
            @RequestParam("artistInput") String artistUpdate
    )  {

        ArtistUpadteInput artistInput = JSONUtil.toBean(artistUpdate, ArtistUpadteInput.class);
        artistService.updateArtist(file,artistInput);
        System.out.println(artistInput);
        return Result.success("Update successfully.");
    }
    @PreAuthorize("hasAnyRole('root', 'admin')")
    @DeleteMapping("deleteArtist/{artistId}/{path}")
    public Result<String> deleteArtist(
            @PathVariable("artistId") long artistId,
            @PathVariable("path") String path
    )  {
        artistService.deleteArtist(artistId,path);
        return Result.success("delete successfully.");
    }
    @PreAuthorize("hasAnyRole('root', 'admin')")
    @GetMapping("getSelect")
    public Result<List<ArtistSelectView>> getArtistSelect(

    ) {
        List<ArtistSelectView> list = artistService.getSelect();
        return Result.success(list);
    }
}
