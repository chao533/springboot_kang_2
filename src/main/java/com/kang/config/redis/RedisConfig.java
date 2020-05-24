package com.kang.config.redis;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kang.mapper.redis.RedisMapper;

/**
 * <p>Title: RedisConfig</p>  
 * <p>Description: redis初始化配置</p>  
 * @author chaokang  
 * @date 2018年12月3日
 */
@EnableCaching
@Configuration
public class RedisConfig {

	@Bean(name = "adminRedisTemplate")
    public RedisTemplate<String, Object> redisTemplate (RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String,Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(redisConnectionFactory);
 
        /*使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value的值(默认使用的是JDK序列化方式)*/
        Jackson2JsonRedisSerializer<Object> json = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        json.setObjectMapper(objectMapper);
 
        /*使用StringRedisSerializer来序列化和反序列化redis的key值*/
        RedisSerializer<?> redisSerializer = new StringRedisSerializer();
 
 
        /*value*/
        template.setValueSerializer(json);
        template.setHashValueSerializer(json);
        /*key*/
        /*key的序列化采用StringRedisSerializer*/
        template.setKeySerializer(redisSerializer);
        template.setHashKeySerializer(new StringRedisSerializer());
 
        template.afterPropertiesSet();
        return template;
    }
	
	@Bean(name = "redisMapper")
	public RedisMapper redisMapper(RedisTemplate<String, Object> redisTemplate) {
		RedisMapper redisMapper = new RedisMapper();
		redisMapper.setRedisTemplate(redisTemplate);
		return redisMapper;
	}

}

