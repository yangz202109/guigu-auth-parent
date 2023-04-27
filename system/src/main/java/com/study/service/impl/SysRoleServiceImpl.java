package com.study.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.domain.system.SysRole;
import com.study.domain.vo.SysRoleQueryVo;
import com.study.service.SysRoleService;
import com.study.mapper.SysRoleMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole>
    implements SysRoleService{

    @Override
    public Page<SysRole> page(SysRoleQueryVo roleQueryVo) {
        return baseMapper.page(new Page<>(roleQueryVo.getPageNum(), roleQueryVo.getPageSize()),roleQueryVo);
    }

    @Override
    public List<SysRole> getRoleByUserId(Long userId) {
        return baseMapper.getRoleByUserId(userId);
    }
}




