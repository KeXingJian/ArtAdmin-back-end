package com.kxj.artadmin.security.filter;

import com.kxj.artadmin.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private CacheManager cacheManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if ("/login".equals(request.getRequestURI()) ||
                "/openapi.yml".equals(request.getRequestURI()) ||
                "/openapi.html".equals(request.getRequestURI()) ||
                "/".equals(request.getRequestURI()) ||
               "/favicon.ico".equals(request.getRequestURI()) ||
                request.getRequestURI().contains("/video/image") ||
                request.getRequestURI().contains("/register/")  ||
                request.getRequestURI().contains("/js/") ||
                request.getRequestURI().contains("/css/") ||
                request.getRequestURI().contains("/img/") ||
                request.getRequestURI().contains("/font/") ||
                request.getRequestURI().contains("/fonts/") ||
                request.getRequestURI().contains("/video/playUserVideo/") ||
                request.getRequestURI().contains("/video/playVideo/")
        ) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = request.getHeader("token");

        if (StringUtils.isEmpty(token)) {
            //404 恶意请求或不合法请求
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            log.warn("token is null");
            return;
        }
        String subject = "";
        try {
            Claims claims = JwtUtil.parseToken(token);
            subject = claims.getSubject();
        } catch (Exception e) {
            //404 恶意请求或不合法请求
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            log.warn("token is error");
            return;
        }
        //取出缓存用户信息
        Cache tokenCache = cacheManager.getCache("tokens");

        if (tokenCache == null) {
            //503 系统级错误
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            log.error("tokenCache is null");
            return;
        }

        Cache.ValueWrapper valueWrapper = tokenCache.get(subject);

        if (valueWrapper == null) {
            //406 token失效
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            log.warn("tokenCache is timeout");
            return;
        }


        UserDetails userDetails = (UserDetails) valueWrapper.get();
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);

    }
}
