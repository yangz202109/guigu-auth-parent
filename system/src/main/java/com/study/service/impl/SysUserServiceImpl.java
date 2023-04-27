package com.study.service.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.custom.CustomUser;
import com.study.domain.system.SysUser;
import com.study.domain.vo.LoginVo;
import com.study.mapper.SysUserMapper;
import com.study.service.SysUserService;
import com.study.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
        implements SysUserService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Override
    public Map<String, Object> login(LoginVo loginVo) {
        Map<String, Object> result = new HashMap<>(3);

        try {
            //1.将前端提交的用户名和密码封装一个 UsernamePasswordAuthenticationToken 对象。
            //2.经过 AuthenticationManager 的认证,如果认证失败会抛出一个 AuthenticationException 错误。
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginVo.getUsername(), loginVo.getPassword()));
            //3.将这个认证过的 Authentication 填入 SecurityContext 里面
            SecurityContextHolder.getContext().setAuthentication(auth);

            CustomUser principal = (CustomUser) auth.getPrincipal();
            SysUser sysUser = principal.getSysUser();
            final String token = JwtUtil.createToken(Convert.toLong(sysUser.getId()), sysUser.getUsername());
            result.put("token", token);
            result.put("userId", sysUser.getId());
            result.put("username", sysUser.getUsername());
        } catch (BadCredentialsException e) {
            log.error("认证失败:{}", e.getMessage());
        }
        return result;
    }

    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }
}




