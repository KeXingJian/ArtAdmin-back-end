package com.kxj.artadmin.controller;

import cn.hutool.json.JSONUtil;
import com.kxj.artadmin.enume.ResultCodeEnum;
import com.kxj.artadmin.model.dto.*;
import com.kxj.artadmin.result.Result;
import com.kxj.artadmin.service.ArtUserService;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/artUser")
@CrossOrigin
public class ArtUserController {

    @Resource
    private ArtUserService artUserService;

    @GetMapping("userInfo/{account}")
    public Result<UserInfoView> getUserInfo(@PathVariable String account) {
        UserInfoView user = artUserService.getUserInfo(account);
        return Result.success(user);
    }

    @GetMapping("simpleUserInfo/{account}")
    public Result<SimpleUserInfoView> getSimpleUserInfo(@PathVariable String account) {
        SimpleUserInfoView user = artUserService.getSimpleInfo(account);
        return Result.success(user);
    }

    @PutMapping ("update")
    public Result<String> adminAddCollection(
           @Nullable @RequestParam("file") MultipartFile file,
           @RequestParam("UpdateUserInput") String input
    ) {
        UpdateUserInput userInput = JSONUtil.toBean(input, UpdateUserInput.class);
        artUserService.update(userInput,file);
        return Result.success("添加成功");
    }

    @GetMapping("getUsers")
    public Result<List<AdminUserView>> getUsers() {
        List<AdminUserView> list = artUserService.getUsers();
        return Result.success(list);
    }

    @PreAuthorize("hasAnyRole('root', 'admin')")
    @GetMapping("changeLockUser/{account}")
    public Result<String> lockUser(@PathVariable String account) {
        if (Objects.equals(account, "2787901285")) {
            return Result.failure("你谁啊!",ResultCodeEnum.FAIL.getCode());
        }
        artUserService.changeLock(account);
        return Result.success("锁定转换成功");
    }

    @GetMapping("getCollectionSelect/{account}")
    public Result<UserCollectionSelectView> collectionSelect(@PathVariable String account) {
            UserCollectionSelectView view = artUserService.getCollectionSelect(account);
            return Result.success(view);
    }

    @PutMapping("addVideoToCollection")
    public Result<String> addVideoToCollection(
            @RequestBody AddUserCollectionInput input
    ){
        artUserService.addVideoToCollection(input);
        return Result.success("添加成功");
    }
}
