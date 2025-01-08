package com.kxj.artadmin.security.service;

import com.kxj.artadmin.model.dto.LoginUserInput;
import com.kxj.artadmin.model.dto.RegisterUserInput;

public interface LoginService {
    String loginCheck(LoginUserInput input);

    void logout();

    String register(RegisterUserInput input,String registerCode);
}
