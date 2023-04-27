package com.study;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yangz
 * @date ${DATE} - ${TIME}
 */
@MapperScan(basePackages = "com.study.mapper")
@SpringBootApplication
public class SystemMain {
    public static void main(String[] args) {
        SpringApplication.run(SystemMain.class,args);
    }
}