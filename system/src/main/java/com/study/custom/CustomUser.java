package com.study.custom;

import com.study.domain.system.SysUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author yangz
 * @date 2022/12/9 - 16:28
 */
public class CustomUser implements UserDetails {
    /**
     * 我们自己的用户实体对象，要调取用户信息时直接获取这个实体对象
     */
    private SysUser sysUser;

    /**
     * 权限集合
     */
    Set<? extends GrantedAuthority> authorities;

    /**
     * 构造函数
     * @param sysUser 实际登录用户实体类对象
     */
    public CustomUser(SysUser sysUser) {
        this.sysUser = sysUser;
        this.authorities = new HashSet<>(1);
    }

    /**
     * 构造函数
     * @param sysUser     实际登录用户实体类对象
     * @param authorities 权限集合
     */
    public CustomUser(SysUser sysUser, Set<? extends GrantedAuthority> authorities) {
        this.sysUser = sysUser;
        this.authorities = authorities;
    }

    public SysUser getSysUser() {
        return sysUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return sysUser.getPassword();
    }

    @Override
    public String getUsername() {
        return sysUser.getUsername();
    }

    /**
     * 账户是否未过期,过期无法验证
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 指定用户是否解锁,锁定的用户无法进行身份验证
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 指示是否已过期的用户的凭据(密码),过期的凭据防止认证
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否可用 ,禁用的用户不能身份验证
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
