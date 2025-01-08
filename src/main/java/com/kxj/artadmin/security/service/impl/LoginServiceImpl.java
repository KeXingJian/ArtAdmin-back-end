package com.kxj.artadmin.security.service.impl;

import com.kxj.artadmin.dao.ArtUserDao;
import com.kxj.artadmin.dao.CollectionDao;
import com.kxj.artadmin.model.dto.DetailUserView;
import com.kxj.artadmin.model.dto.RegisterUserInput;
import com.kxj.artadmin.model.dto.UserCollectionInput;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.cache.Cache;
import com.kxj.artadmin.model.dto.LoginUserInput;
import com.kxj.artadmin.security.service.LoginService;
import com.kxj.artadmin.utils.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Objects;


@Service
public class LoginServiceImpl implements LoginService {
    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private ArtUserDao artUserDao;

    @Resource
    private CollectionDao collectionDao;

    @Value("${register.token}")
    private String registerToken;

    private final Cache tokenCache;

    public LoginServiceImpl(CacheManager cacheManager) {
        this.tokenCache = cacheManager.getCache("tokens");
    }

    @Override
    public String loginCheck(LoginUserInput input) {
        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(input.getAccount(), input.getPassword());
        try {
            Authentication authenticate = authenticationManager.authenticate(authRequest);
            DetailUserView detailUserView = artUserDao.selectUserByName(input.getAccount());
            artUserDao.changeStatus(detailUserView);
            if (authenticate.isAuthenticated() && detailUserView.getRole() != null) {
                SecurityContextHolder.getContext().setAuthentication(authenticate);
                String subject = "user:" + ((UserDetails) authenticate.getPrincipal()).getUsername()+"$"+detailUserView.getRole().getName();
                String token = JwtUtil.createToken(subject, 1000 * 60 * 60 * 24 * 7);
                // 将token存储到缓存中
                tokenCache.put(subject, authenticate.getPrincipal());
                return token;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void logout() {
        // 获取当前认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            // 如果存在认证主体，则尝试从缓存中移除对应的token
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String subject = "user:" + userDetails.getUsername();

                // 从缓存中移除token
                tokenCache.evict(subject);
            }
            // 清除安全上下文中的认证信息
            SecurityContextHolder.clearContext();
        }
    }

    @Override
    public String register(RegisterUserInput input, String registerCode) {
        if (!Objects.equals(registerCode, registerToken)) {
            return null;
        }
        //查询账号是否重复
        DetailUserView user = artUserDao.selectUserByName(input.getAccount());
        if (user != null) {
            return "0";
        }

        //注册用户
        artUserDao.register(input);
        //注册favorite
        UserCollectionInput collectionInput = new UserCollectionInput();
        collectionInput.setName("My Favorite");
        collectionInput.setCommon(false);
        UserCollectionInput.TargetOf_artUser targetOfArtUser = new UserCollectionInput.TargetOf_artUser();
        targetOfArtUser.setAccount(input.getAccount());
        collectionInput.setArtUser(targetOfArtUser);

        collectionDao.userAddCollection(collectionInput);

        DetailUserView detailUser = artUserDao.selectUserByName(input.getAccount());
        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(input.getAccount(), input.getPassword());
        try {
            Authentication authenticate = authenticationManager.authenticate(authRequest);
            if (authenticate.isAuthenticated() && detailUser.getRole() != null) {
                SecurityContextHolder.getContext().setAuthentication(authenticate);
                String subject = "user:" + ((UserDetails) authenticate.getPrincipal()).getUsername()+"$"+detailUser.getRole().getName();
                String token = JwtUtil.createToken(subject, 1000 * 60 * 60 * 24 * 7);
                // 将token存储到缓存中
                tokenCache.put(subject, authenticate.getPrincipal());
                return token;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
