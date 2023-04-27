package com.study.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.study.domain.system.SysRole;
import com.study.domain.vo.SysRoleQueryVo;

import java.util.List;

/**
 *
 */
public interface SysRoleService extends IService<SysRole> {

    Page<SysRole> page(SysRoleQueryVo roleQueryVo);

    List<SysRole> getRoleByUserId(Long userId);
}
