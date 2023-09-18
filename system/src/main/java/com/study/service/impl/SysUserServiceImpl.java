package com.study.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.constant.CacheConstants;
import com.study.custom.CustomUser;
import com.study.domain.system.SysUser;
import com.study.domain.vo.LoginVo;
import com.study.exception.BusinessException;
import com.study.mapper.SysUserMapper;
import com.study.service.SysUserService;
import com.study.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
        implements SysUserService {

    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Map<String, Object> login(LoginVo loginVo) {
        Map<String, Object> result = new HashMap<>(3);

        if (isErrorNum(loginVo.getUsername())) {
            throw new BusinessException(500, "登录错误次数达到上限,请稍后再试");
        }

        try {
            //1.将前端提交的用户名和密码封装一个 UsernamePasswordAuthenticationToken 对象。
            //2.经过 AuthenticationManager 的认证,如果认证失败会抛出一个 AuthenticationException 错误。
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginVo.getUsername(), loginVo.getPassword()));
            //3.将这个认证过的 Authentication 填入 SecurityContext 里面
            SecurityContextHolder.getContext().setAuthentication(auth);

            CustomUser principal = (CustomUser) auth.getPrincipal();
            SysUser sysUser = principal.getSysUser();
            final String token = JwtUtil.createToken(sysUser.getId(), sysUser.getUsername());
            result.put("token", token);
            result.put("userId", sysUser.getId());
            result.put("username", sysUser.getUsername());

            //保存当前登录成功用户的token
            redisTemplate.opsForValue().set(CacheConstants.LOGIN_TOKEN_KEY + sysUser.getId(), token,
                    CacheConstants.TOKEN_EXPIRATION, TimeUnit.SECONDS);
        } catch (BadCredentialsException e) {
            //认证失败记录该用户密码错误次数
            String key = CacheConstants.PWD_ERR_CNT_KEY + loginVo.getUsername();
            String script = "redis.call('INCRBY',KEYS[1],1) redis.call('EXPIRE',KEYS[1],ARGV[1])";
            Long execute = redisTemplate.execute(new DefaultRedisScript<>(script, Long.class),
                    Collections.singletonList(key), CacheConstants.PWD_ERR_EXPIRATION);
            log.info("认证失败:{},记录失败:{}", e.getMessage(), execute);
        }
        return result;
    }

    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }

    public Boolean isErrorNum(String username) {
        String key = CacheConstants.PWD_ERR_CNT_KEY + username;
        String script = "if redis.call('exists',KEYS[1]) == 0 then return 0\n" +
                "elseif \n" +
                "  redis.call('get',KEYS[1]) < ARGV[1] then  return 0 \n" +
                "else\n" +
                "   return 1\n" +
                "end";
        return redisTemplate.execute(new DefaultRedisScript<>(script, Boolean.class),
                Collections.singletonList(key), CacheConstants.PWD_ERR_NUM);
    }
}




