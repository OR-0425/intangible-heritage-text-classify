package com.example.intangibleheritagedemo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {

    // 密钥，至少32字节，不要随便改
    private static final String SECRET = "abcdefghabcdefghabcdefghabcdefgh";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
    // 过期时间 2小时
    private static final long EXPIRATION = 2 * 60 * 60 * 1000;

    /**
     * 生成token（静态方法，直接 JwtUtil.generateToken 调用）
     */
    public static String generateToken(Long userId, String username, String role) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + EXPIRATION);
        return Jwts.builder()
                .subject(username)
                .claim("userId", userId)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expireDate)
                .signWith(KEY)
                .compact();
    }

    /**
     * 解析token，拿到claims（拦截器里面用）
     */
    public static Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
