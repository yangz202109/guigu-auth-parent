package com.study.utils;

/**
 * @author yangz
 * @date 2022/12/8 - 14:28
 * <p>
 * 生成JSON Web令牌的工具类
 */

import cn.hutool.core.util.StrUtil;
import com.study.constant.CacheConstants;
import io.jsonwebtoken.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    //加密的秘钥
    private static final String tokenSignKey = "123456";

    //根据用户id和名称生成字符串
    public static String createToken(String userId, String username) {
        return Jwts.builder()
                .setSubject("AUTH-USER")//主题
                .setExpiration(new Date(System.currentTimeMillis() + CacheConstants.TOKEN_EXPIRATION))//设置过期时间(当前时间+tokenExpiration)
                .claim("userId", userId)//自定义的字段
                .claim("username", username)
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)//根据指定加密规则和秘钥进行加密
                .compressWith(CompressionCodecs.GZIP)//将字符串进行压缩到一行
                .compact();
    }


    //从token中解析获取用户名称
    public static Long getUserId(String token) {
        try {
            if (StrUtil.isEmpty(token)) return null;

            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            return (Long) claims.get("userId");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getUsername(String token) {
        try {
            if (StrUtil.isEmpty(token)) return "";

            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            return (String) claims.get("username");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Map<String, String> getUsernameAndUserId(String token) {
        Map<String, String> data = new HashMap<>(2);
        try {
            if (StrUtil.isEmpty(token)) return null;

            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            data.put("username", (String) claims.get("username"));
            data.put("userId", (String) claims.get("userId"));
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void removeToken(String token) {
        //jwttoken无需删除，客户端扔掉即可。
    }

    public static void main(String[] args) {
        String token = JwtUtil.createToken("123456", "yangz");
        System.out.println(token);
        System.out.println(JwtUtil.getUserId(token));
        System.out.println(JwtUtil.getUsername(token));
    }
}
