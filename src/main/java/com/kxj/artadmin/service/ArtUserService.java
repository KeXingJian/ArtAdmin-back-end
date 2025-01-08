package com.kxj.artadmin.service;

import com.kxj.artadmin.model.dto.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArtUserService {

    UserInfoView getUserInfo(String account);

    SimpleUserInfoView getSimpleInfo(String account);

    void update(UpdateUserInput userInput, MultipartFile file);

    List<AdminUserView> getUsers();

    void changeLock(String account);

    UserCollectionSelectView getCollectionSelect(String account);

    void addVideoToCollection(AddUserCollectionInput input);
}
