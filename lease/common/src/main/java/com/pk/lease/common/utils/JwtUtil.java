package com.pk.lease.common.utils;

import com.pk.lease.common.exception.LeaseException;
import com.pk.lease.common.result.ResultCodeEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

public class JwtUtil {


    //private static final String SECRET_KEY = "pankun20223048204913553595888111111111111111111";  // 密钥
    //private static final long EXPIRATION_TIME = 3600_000;


    private static long tokenExpiration = 60 * 60 * 1000L;
    private static SecretKey tokenSignKey = Keys.hmacShaKeyFor("M0PKKI6pYGVWWfDZw90a0lTpGYX1d4AQ".getBytes());

    public static String createToken(Long userId, String username) {
        String token = Jwts.builder().
                subject("USER_INFO").
                expiration(new Date(System.currentTimeMillis() + tokenExpiration)).
                claim("userId", userId).
                claim("username", username).
                signWith(tokenSignKey).
                compact();
        return token;
    }

    public static Claims parseToken(String token) {

        if(token==null){
            throw new LeaseException(ResultCodeEnum.ADMIN_LOGIN_AUTH.getCode(),ResultCodeEnum.ADMIN_LOGIN_AUTH.getMessage());
        }

        try {
            return Jwts.parser()
                    .verifyWith(tokenSignKey)  // 设置验证密钥
                    .build()
                    .parseSignedClaims(token)   // 解析并验证签名
                    .getPayload();              // 提取声明（Payload）
        } catch (ExpiredJwtException e) {
            throw new LeaseException(ResultCodeEnum. TOKEN_EXPIRED.getCode(),ResultCodeEnum. TOKEN_EXPIRED.getMessage() );
        }catch (JwtException e) {
            throw new LeaseException(ResultCodeEnum. TOKEN_INVALID.getCode(),ResultCodeEnum. TOKEN_INVALID.getMessage());
        }
    }

    public static boolean validateToken(String token) {
        Claims claims = parseToken(token);
        return claims != null && !claims.getExpiration().before(new Date());
    }

    public static void main(String[] args) {
        // 测试生成和解析 Token
        String token = createToken(2L, "user");
        System.out.println("Generated Token: " + token);

        Claims claims = parseToken(token);
        if (claims != null) {
            System.out.println("UserID: " + claims.get("userId"));
            System.out.println("Username: " + claims.get("username"));
            System.out.println("Expiration: " + claims.getExpiration());
        } else {
            System.out.println("Invalid Token");
        }
    }

}
