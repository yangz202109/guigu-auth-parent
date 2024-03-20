package com.study.domain.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 登录对象
 */
@Getter
@Setter
public class LoginVo {

    /**
     * 手机号
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}
