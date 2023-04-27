package com.study.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.study.domain.system.SysMenu;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * @author yangz
 * @date 2022/12/13 - 14:51
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    List<SysMenu> getMenusByRoleId(@Param("roleIds") List<Long> roleIds);
}
