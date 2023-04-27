package com.study;

import com.study.domain.system.SysUser;
import com.study.mapper.SysUserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author yangz
 * @date 2022/12/5 - 8:57
 */
@SpringBootTest
public class MyTest {
    @Resource
    private SysUserMapper userMapper;

    @Test
    public void t1(){
        List<SysUser> sysUsers = userMapper.selectList(null);
        sysUsers.forEach(System.out::println);

    }
}
