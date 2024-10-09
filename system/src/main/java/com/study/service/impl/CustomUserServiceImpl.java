package com.study.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.study.custom.CustomUser;
import com.study.domain.system.SysMenu;
import com.study.domain.system.SysUser;
import com.study.mapper.SysUserMapper;
import com.study.service.SysMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author yangz
 * @date 2022/12/9 - 16:44
 * 自定义校验登录名称业务类
 */
@Slf4j
@Service
public class CustomUserServiceImpl implements UserDetailsService {
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysMenuService sysMenuService;

    @Override
    public CustomUser loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("开始登陆验证,用户名为: {}", username);

        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        SysUser sysUser = sysUserMapper.selectOne(wrapper);

        if (sysUser == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        if (sysUser.getStatus() == 0) {
            throw new RuntimeException("该账号已被封禁");
        }

        //获取当前用户有权限的菜单
        List<SysMenu> menus = sysMenuService.getMenuByUserId(Long.parseLong(sysUser.getId()));

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        for (SysMenu menu : menus) {
            authorities.add(new SimpleGrantedAuthority(menu.toString()));
        }
        return new CustomUser(sysUser, authorities);
    }
}
