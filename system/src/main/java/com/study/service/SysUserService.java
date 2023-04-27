package com.study.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.domain.system.SysUser;
import com.study.domain.vo.LoginVo;
import java.util.Map;

/**
 *
 */
public interface SysUserService extends IService<SysUser> {
    Map<String,Object> login(LoginVo loginVo);

    void logout();
}
