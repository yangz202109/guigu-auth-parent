package com.study.custom;

import cn.hutool.crypto.SecureUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author yangz
 * @date 2022/12/9 - 16:20
 * 自定义密码组件(设置密码的加密和比较规则)
 */
@Component
public class CustomMd5PasswordEncoder implements PasswordEncoder {

    //加密
    @Override
    public String encode(CharSequence rawPassword) {
        return SecureUtil.md5(rawPassword.toString());
    }

    //比较
    @Override
    public boolean matches(CharSequence rawPassword, String s) {
        return s.equals(SecureUtil.md5(rawPassword.toString()));
    }
}
