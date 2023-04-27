package com.study.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.domain.system.SysMenu;
import com.study.domain.system.SysRole;
import com.study.mapper.SysMenuMapper;
import com.study.mapper.SysRoleMapper;
import com.study.service.SysMenuService;
import com.study.utils.TreeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yangz
 * @date 2022/12/13 - 14:54
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu>
        implements SysMenuService {
    @Resource
    private SysRoleMapper roleMapper;

    @Override
    public List<SysMenu> getMenuByUserId(Long userId) {
        List<SysMenu> menus = new ArrayList<>();

        //判断该用户的角色
        List<SysRole> roles = roleMapper.getRoleByUserId(userId);
        if (roles == null || roles.isEmpty()){
            return menus;
        }

        boolean isAdmin = false;
        for (SysRole role : roles) {
            if (role.getRoleCode().equals("admin")) {
                isAdmin = true;
                break;
            }
        }

        if (isAdmin) {
            //是管理员
            QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();
            wrapper.eq("status", 1)
                    .orderByAsc("sort_value");
            menus = baseMapper.selectList(wrapper);
        } else {
            menus = baseMapper.getMenusByRoleId(roles.stream().map(role -> Long.parseLong(role.getId())).collect(Collectors.toList()));
        }
        return TreeUtil.build(menus);
    }
}
