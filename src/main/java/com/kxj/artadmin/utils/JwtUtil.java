package com.kxj.artadmin.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.UUID;

/**
 * JWT工具类
 */
public class JwtUtil {

    //设置秘钥明文
    public static final String JWT_KEY = "2787901285q";

    public static String createToken( String subject, long ttlMillis) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setId(UUID.randomUUID().toString())
                .setIssuer("kxj")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ttlMillis))
                .setSubject(subject)
                .signWith(SignatureAlgorithm.HS256, JWT_KEY)
                .compact();

    }
    public static Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(JWT_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
