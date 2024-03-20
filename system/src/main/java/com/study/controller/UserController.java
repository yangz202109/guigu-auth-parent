package com.study.controller;

import com.study.domain.ResultData;
import com.study.domain.system.SysUser;
import com.study.domain.vo.LoginVo;
import com.study.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yangz
 * @date 2022/12/5 - 9:38
 */
@Api(tags = "用户管理")
@Slf4j
@RestController
@RequestMapping("/system/user")
public class UserController {
    @Resource
    private SysUserService userService;

    @ApiOperation("获取全部用户")
    @GetMapping("/findAll")
    public ResultData<List<SysUser>> findAll() {
        return ResultData.ok(userService.list());
    }

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public ResultData<String> register(@RequestBody LoginVo loginVo) {
        boolean inSuccess = userService.addUser(loginVo);
        if (inSuccess) {
            return ResultData.ok();
        } else {
            return ResultData.error("添加失败");
        }
    }

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public ResultData<Map<String, Object>> login(@RequestBody LoginVo loginVo) {
        //log.info("进入自定义登录接口....");
        Map<String, Object> result = userService.login(loginVo);
        if (result == null || result.isEmpty()) {
            return ResultData.error("用户名或密码错误");
        }
        return ResultData.ok(result);
    }

    @ApiOperation("用户登出")
    @PostMapping("/logout")
    public ResultData<String> logout() {
        userService.logout();
        return ResultData.ok("success");
    }

    @ApiOperation("查看自己信息")
    @GetMapping("/lookSelf")
    public ResultData<Map<String, Object>> lookMe() {
        Map<String, Object> msg = new HashMap<>(1);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        msg.put("userInfo", authentication.getPrincipal());
        return ResultData.ok(msg);
    }
}
