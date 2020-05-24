package com.kang.common.aop;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;
import com.kang.common.anno.RequestLimit_2;
import com.kang.common.exception.RequestLimitException;
import com.kang.common.utils.IPUtils;

/**
　 * <p>Title: RequestLimitAspect</p> 
　 * <p>Description: IP限流（令牌桶）</p> 
　 * @author CK 
　 * @date 2020年4月16日
 */
@Aspect
@Component
public class LimitAspect_2 {
	
	private Logger logger = LoggerFactory.getLogger(LimitAspect_2.class);
	
	//根据IP分不同的令牌桶, 每天自动清理缓存
	private static LoadingCache<String, RateLimiter> caches = CacheBuilder.newBuilder()
			.maximumSize(1000)
			.expireAfterWrite(1, TimeUnit.DAYS)
			.build(new CacheLoader<String, RateLimiter>() {
				@Override
				public RateLimiter load(String key) {
					// 新的IP初始化 每秒只发出5个令牌
					return RateLimiter.create(0.5);
				}
			});
	
	
	@Pointcut("@annotation(com.kang.common.anno.RequestLimit_2)")
    private void limitPointCut_rateLimiter(){}

	@Around("limitPointCut_rateLimiter()")
	public Object requestLimit(ProceedingJoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		RequestLimit_2 limitAnnotation = method.getAnnotation(RequestLimit_2.class);
		RequestLimit_2.LimitType limitType = limitAnnotation.limitType();
		String key = limitAnnotation.key();
		Object obj;
		try {
			if(limitType.equals(RequestLimit_2.LimitType.IP)){
				key = IPUtils.getIpAddr();
				logger.info("访问IP地址为：{}",key);
			}
			RateLimiter rateLimiter = caches.get(key);
			Boolean flag = rateLimiter.tryAcquire();
			if(flag){
				obj = joinPoint.proceed();
			}else{
				throw new RequestLimitException("小同志，你访问的太频繁了");
			}
		} catch (Throwable e) {
			throw new RequestLimitException("小同志，你访问的太频繁了");
		}
		return obj;
		
	}
}
