package com.study.utils;

import cn.hutool.core.convert.Convert;
import com.study.domain.system.SysMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yangz
 * @date 2022/12/13 - 15:36
 */
public class TreeUtil {

    public static List<SysMenu> build(List<SysMenu> menus) {
        if (menus == null){
            return null;
        }

        List<SysMenu> root = new ArrayList<>();

        for (SysMenu menu : menus) {
            if (menu.getParentId() == 0L) {
                root.add(findChildren(menu, menus));
            }
        }
        return root;
    }

    public static SysMenu findChildren(SysMenu parentMenu, List<SysMenu> menus) {
        parentMenu.setChildren(new ArrayList<>());

        for (SysMenu menu : menus) {
            if (menu.getParentId().equals(Convert.toLong(parentMenu.getId()))) {
                parentMenu.getChildren().add(findChildren(menu, menus));
            }
        }
        return parentMenu;
    }

}
