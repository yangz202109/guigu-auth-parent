package com.study.utils;

import cn.hutool.core.lang.generator.SnowflakeGenerator;

/**
 * @author yangz
 * @createTime 2023/12/7 - 15:10
 */
public class IdSnowflakeUtil {
    private static final SnowflakeGenerator snowflakeGenerator;

    static {
        snowflakeGenerator = new SnowflakeGenerator();
    }

    public static Long getId(){
        return snowflakeGenerator.next();
    }
}
