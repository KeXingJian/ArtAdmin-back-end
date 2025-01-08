package com.kxj.artadmin.controller;


import cn.hutool.json.JSONUtil;
import com.kxj.artadmin.model.dto.AddPictureInput;
import com.kxj.artadmin.model.dto.MirrorPictureView;
import com.kxj.artadmin.result.Result;
import com.kxj.artadmin.service.PictureService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("picture")
@CrossOrigin
public class PictureController {

    @Resource
    private PictureService pictureService;

    @GetMapping("getMirrors")
    public Result<List<MirrorPictureView>> getMirrors() {
        List<MirrorPictureView> list = pictureService.getMirrors();
        return Result.success(list);
    }

    @PostMapping("addMirror")
    public Result<String> addMirror(
            @RequestParam("file") MultipartFile file
    ) {
        pictureService.addPicture(file);
        return Result.success("添加成功");
    }

    @DeleteMapping("deleteMirror/{id}")
    public Result<String> deleteMirror(
            @PathVariable Long id
    ){
        pictureService.deleteById(id);
        return Result.success("删除成功");

    }

}
