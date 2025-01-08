package com.kxj.artadmin.service.impl;

import com.kxj.artadmin.config.UploadConfig;
import com.kxj.artadmin.dao.ArtUserDao;
import com.kxj.artadmin.enume.Type;
import com.kxj.artadmin.model.ArtUser;
import com.kxj.artadmin.model.dto.*;
import com.kxj.artadmin.service.ArtUserService;
import com.kxj.artadmin.utils.FileUtil;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
public class ArtUserServiceImpl implements ArtUserService {

    @Resource
    private ArtUserDao artUserDao;

    @Resource
    private UploadConfig uploadConfig;

    @Override
    public UserInfoView getUserInfo(String account) {
        return artUserDao.getInfo(UserInfoView.class, account);
    }

    @Override
    public SimpleUserInfoView getSimpleInfo(String account) {
        return artUserDao.getInfo(SimpleUserInfoView.class, account);
    }

    @Override
    public void update(UpdateUserInput userInput, MultipartFile file) {

        if (file != null && userInput.getPicture() != null) {
            //迁移
            String newPath = FileUtil.uploadPicture(file, uploadConfig.getPictureDir() + "/" + Type.AVATAR.getPath());
            userInput.getPicture().setPath(newPath);
        }

        artUserDao.update(userInput);
    }

    @Override
    public List<AdminUserView> getUsers() {
        return artUserDao.getUsers();
    }

    @Override
    public void changeLock(String account) {
        artUserDao.changeLock(account);
    }

    @Override
    public UserCollectionSelectView getCollectionSelect(String account) {
        return artUserDao.getCollectionSelect(account);
    }

    @Override
    public void addVideoToCollection(AddUserCollectionInput input) {
        artUserDao.addVideoToCollection(input);
    }
}
