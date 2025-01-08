package com.kxj.artadmin.controller;

import cn.hutool.json.JSONUtil;
import com.kxj.artadmin.enume.ResultCodeEnum;
import com.kxj.artadmin.model.dto.*;
import com.kxj.artadmin.result.Result;
import com.kxj.artadmin.service.CollectionService;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/collection")
@CrossOrigin
public class CollectionController {

    @Resource
    private CollectionService collectionService;


    @GetMapping("publicCollections")
    public Result<List<PublicCollectionView>> getPublicCollections(){
        List<PublicCollectionView> list = collectionService.getPublic();
        return Result.success(list);
    }


    @PreAuthorize("hasAnyRole('root', 'admin')")
    @PostMapping("adminAddCollection")
    public Result<String> adminAddCollection(
            @RequestParam("file") MultipartFile file,
            @RequestParam("collectionInput") String collectionAdd
    ) {
        if (file.isEmpty()) {
            return Result.failure(ResultCodeEnum.Empty.getMessage()
                    , ResultCodeEnum.Empty.getCode());
        }
        AdminCollectionInput collectionInput = JSONUtil.toBean(collectionAdd, AdminCollectionInput.class);
        collectionInput.setCommon(true);
        collectionService.adminAddCollection(collectionInput,file);
        return Result.success("添加成功");
    }

    @PreAuthorize("hasAnyRole('root', 'admin')")
    @GetMapping("getCollections")
    public Result<List<AdminCollectionView>> getCollections(){
        List<AdminCollectionView> list = collectionService.getList();
        return Result.success(list);
    }
    @PreAuthorize("hasAnyRole('root', 'admin')")
    @PutMapping("adminUpdateCollection")
    public Result<String> adminUpdateCollection(
            @Nullable @RequestParam("file") MultipartFile file,
            @RequestParam("collectionInput") String collectionUpdate
    ){
        AdminCollectionInput collectionInput = JSONUtil.toBean(collectionUpdate, AdminCollectionInput.class);
        collectionInput.setCommon(true);
        collectionService.updateCollection(collectionInput,file);
        return Result.success("Update successfully.");
    }

    @PreAuthorize("hasAnyRole('root', 'admin')")
    @PutMapping("addVideo")
    public Result<String> addVideo(
            @RequestBody VideoCollectionInput input
    ){
        collectionService.addVideo(input);
        return Result.success("Add video successfully.");
    }

    @PreAuthorize("hasAnyRole('root', 'admin')")
    @DeleteMapping("delete/{id}")
    public Result<String> delete(
           @PathVariable long id
    ){
        collectionService.delete(id);
        return Result.success("Delete successfully.");
    }
    @PreAuthorize("hasAnyRole('root', 'admin')")
    @DeleteMapping("deleteVideo/{id}/{uuid}")
    public Result<String> deleteVideo(
            @PathVariable long id,
            @PathVariable String uuid
    ){
        collectionService.deleteVideo(id,uuid);
        return Result.success("Delete video successfully.");
    }

    @PostMapping("upsertByUser")
    public Result<String> upsertByUser(
            @RequestBody UserCollectionInput userCollectionInput
    ){
        collectionService.upsertByUser(userCollectionInput);
        return Result.success("Upsert successfully.");
    }
}
