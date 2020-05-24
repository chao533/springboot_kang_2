package com.kang.config.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Setter;

/**
　 * <p>Title: RedissonConfig</p> 
　 * <p>Description: redisson分布式锁配置文件</p> 
　 * @author CK 
　 * @date 2020年4月16日
 */
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
public class RedissonConfig {
	
    private String host;

    private String port;

//    private String password;

    private int database;

    @Bean
    public RedissonClient getRedisson(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + host + ":" + port).setDatabase(database);
        //一共有三种模式：单击、哨兵、集群，根据实际需要配置即可
        return Redisson.create(config);
    }
}

