package com.study.utils;

import cn.hutool.core.convert.Convert;
import com.study.domain.base.MyTreeNote;
import com.study.domain.system.SysMenu;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yangz
 * @code 2022/12/13 - 15:36
 */
public class TreeUtil {

    public static List<SysMenu> buildMenu(List<SysMenu> menus) {
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


    /**
     * 通用树生成方法
     * @param list 原始集合数据
     * @param <T> 原始数据类型(必须实现自定义树生成接口)
     * @return 树结构数据
     */
    public static <T extends MyTreeNote<T>> List<T> build(List<T> list) {
        //第一次遍历: 记录节点间的父子关系
        Map<T, List<T>> relations = new HashMap<>();
        for (T node : list) {
            List<T> relation = relations.computeIfAbsent(node.parent(), (p) -> new LinkedList<>());
            relation.add(node);
        }
        List<T> roots = relations.get(null); //父节点为null的即为根节点
        //第二次遍历: 根据父子关系建立树
        Stack<T> stack = roots.stream().collect(Collectors.toCollection(Stack::new));
        while (!stack.isEmpty()) {
            T node =  stack.pop();
            node.setChildren(relations.getOrDefault(node, Collections.emptyList()));
            stack.addAll(node.getChildren());
        }
        return roots;
    }

}
