package com.kxj.artadmin.security.controller;

import com.kxj.artadmin.enume.ResultCodeEnum;
import com.kxj.artadmin.model.dto.LoginUserInput;
import com.kxj.artadmin.model.dto.RegisterUserInput;
import com.kxj.artadmin.result.Result;
import com.kxj.artadmin.security.service.LoginService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class LoginController {

    @Resource
    private LoginService loginService;

    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginUserInput input) {
        String token = loginService.loginCheck(input);
        if (token == null) {
            return Result.failure("登录失败",ResultCodeEnum.FAIL.getCode());
        }
        return Result.success(token);
    }

    @GetMapping("/logout")
    public Result<String> logout() {
        loginService.logout();
        return Result.success("登出成功");
    }

    @PostMapping("/register/{registerCode}")
    public Result<String> register(
            @RequestBody RegisterUserInput input,
            @PathVariable String registerCode
    ) {

        String token = loginService.register(input,registerCode);
        if (token == null) {
            return Result.failure("注册失败,在关于处联系管理员",ResultCodeEnum.FAIL.getCode());
        }else if(token.equals("0")){
            return Result.failure("账号已存在",ResultCodeEnum.DATA_ERROR.getCode());
        }
        return Result.success(token);
    }

}
