package com.kxj.artadmin.security.service.impl;

import com.kxj.artadmin.dao.ArtUserDao;
import com.kxj.artadmin.model.dto.DetailUserView;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
@Service
public class ArtUserDetailsService implements UserDetailsService {
    @Resource
    private ArtUserDao artUserDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        DetailUserView detailUserView = artUserDao.selectUserByName(username);
        return User.builder()
                .username(detailUserView.getAccount())
                .password(detailUserView.getPassword())
                .roles(Objects.requireNonNull(detailUserView.getRole()).getName())
                .accountLocked(detailUserView.isStop())
                .build();
    }
}
