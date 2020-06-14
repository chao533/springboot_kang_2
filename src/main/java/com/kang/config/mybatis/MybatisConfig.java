package com.kang.config.mybatis;

import org.springframework.context.annotation.Configuration;

import tk.mybatis.spring.annotation.MapperScan;

@Configuration
@MapperScan("com.kang.mapper.mybatis")
public class MybatisConfig {

}
