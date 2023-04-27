package com.study.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.domain.system.SysMenu;
import java.util.List;

/**
 * @author yangz
 * @date 2022/12/13 - 14:53
 */
public interface SysMenuService extends IService<SysMenu> {
    /**
     * 查询当前用户有权限的菜单
     * @param userId 用户id
     * @return 菜单列表
     */
    List<SysMenu> getMenuByUserId(Long userId);
}
