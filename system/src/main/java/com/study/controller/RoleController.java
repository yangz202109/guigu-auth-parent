package com.study.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.study.domain.ResultData;
import com.study.domain.system.SysRole;
import com.study.domain.vo.SysRoleQueryVo;
import com.study.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

/**
 * @author yangz
 * @date 2022/12/5 - 9:52
 */
@Api(tags = "角色管理")
@RestController
@RequestMapping("/system/role")
public class RoleController {
    @Resource
    private SysRoleService roleService;

    @ApiOperation("角色分页")
    @GetMapping("/page")
    public ResultData<Page<SysRole>> page(SysRoleQueryVo roleQueryVo) {
        Page<SysRole> page = roleService.page(roleQueryVo);
        return ResultData.ok(page);
    }

    @ApiOperation("角色添加")
    @PostMapping("/save")
    public ResultData<String> save(@RequestBody SysRole role) {
        boolean isSuccess = roleService.save(role);
        if (isSuccess) {
            return ResultData.ok();
        } else {
            return ResultData.error("添加失败");
        }
    }
}
