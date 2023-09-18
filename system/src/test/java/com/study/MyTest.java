package com.study;

import com.study.domain.system.SysUser;
import com.study.mapper.SysUserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author yangz
 * @date 2022/12/5 - 8:57
 */
@SpringBootTest
public class MyTest {
    @Resource
    private SysUserMapper userMapper;
    @Resource
    private RedisTemplate<String ,Object> redisTemplate;
    @Autowired
    private ApplicationContext applicationContext;// 获取指定类型的Bean

    @Test
    public void t1(){
        List<SysUser> sysUsers = userMapper.selectList(null);
        sysUsers.forEach(System.out::println);

    }
    @Test
    public void t2(){
        RedisSerializer<?> keySerializer = redisTemplate.getKeySerializer();
        RedisSerializer<?> valueSerializer = redisTemplate.getValueSerializer();
        System.out.println(keySerializer);
        System.out.println(valueSerializer);
        System.out.println(redisTemplate);
        //redisTemplate.opsForValue().set("k1","v1");
    }

    @Test
    public void t3(){
        Map<String, RedisTemplate> beansOfType =
                applicationContext.getBeansOfType(RedisTemplate.class);
        System.out.println(beansOfType);
    }
}
