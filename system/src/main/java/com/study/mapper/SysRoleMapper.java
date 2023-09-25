package com.study.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.study.domain.system.SysRole;
import com.study.domain.vo.SysRoleQueryVo;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * @Entity com.study.domain.SysRole
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    Page<SysRole> page(Page<SysRole> sysRolePage, @Param("vo") SysRoleQueryVo roleQueryVo);

    List<SysRole> getRoleByUserId(@Param("userId") Long userId);
}




