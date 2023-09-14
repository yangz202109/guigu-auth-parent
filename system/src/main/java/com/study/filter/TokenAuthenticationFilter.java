package com.study.filter;

import cn.hutool.core.util.StrUtil;
import com.study.constant.CacheConstants;
import com.study.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * @author yangz
 * @date 2022/12/12 - 13:46
 * 自定义认证解析token过滤器
 * SecurityContext：上下文对象,Authentication 对象会放在里面。
 * SecurityContextHolder：用于拿到上下文对象的静态工具类。
 * Authentication：认证接口,定义了认证对象的数据形式。
 * AuthenticationManager：用于校验 Authentication,返回一个认证完成后的 Authentication 对象。
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(TokenAuthenticationFilter.class);
    private final RedisTemplate<String, Object> redisTemplate;

    public TokenAuthenticationFilter(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("访问uri:{}", request.getRequestURI());

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if (null != authentication) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

    /**
     * 解析request中的token数据
     *
     * @param request 请求
     * @return 登录参数封装类
     */
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        //从请求中取出token
        String token = request.getHeader("Authorization");
        logger.info("请求头token:" + token);

        if (StrUtil.isNotEmpty(token)) {
            Map<String, String> tokeInfo = JwtUtil.getUsernameAndUserId(token);
            if (tokeInfo != null) {
                String userId = tokeInfo.get("userId");
                String redisKey = CacheConstants.LOGIN_TOKEN_KEY + userId;
                //获取该用户最新的token
                String userToken = (String) redisTemplate.opsForValue().get(redisKey);
                if (userToken != null && userToken.equals(token)) {
                    return new UsernamePasswordAuthenticationToken(tokeInfo, null, Collections.emptyList());
                }
            }
        }
        return null;
    }
}
