package com.kang.service.impl;

import java.util.List;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kang.common.constant.RedisConstants;
import com.kang.common.msg.ErrorCode;
import com.kang.common.msg.Message;
import com.kang.config.redis.RedissonLock;
import com.kang.mapper.redis.RedisMapper;
import com.kang.service.RedPacketService;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RedPacketServiceImpl implements RedPacketService {
	
	private static int corePoolSize = Runtime.getRuntime().availableProcessors();
	//创建线程池  调整队列数 拒绝服务
	private static ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, corePoolSize + 1, 10l, TimeUnit.SECONDS,
			new LinkedBlockingQueue<>(1000));

	@Autowired
	private RedissonLock redissonLock;
	@Autowired
	private RedisMapper redisMapper;

	@Override
	public Message<?> rushRedPacket_RedissonLock(Long userId, Long packetId) {
		Integer price = null;
		boolean res = false;
		try {
			res = redissonLock.tryLock(RedisConstants.KANG_REDISSON_LOCK_PACKET + packetId, TimeUnit.SECONDS, 3, 20);
			if (res) {
				Long num = redisMapper.decr(RedisConstants.KANG_RED_PACKET_NUM + packetId, 1);
				if (num < 0) {
					// 红包已经没有了
					return new Message<>(ErrorCode.ERROR_RED_PACKET);
				} else {
					if (num == 0) {
						// 最后一个红包了
						price = new Integer(redisMapper.get(RedisConstants.KANG_RED_PACKET_PRICE + packetId).toString());
					} else {
						// 计算红包还剩多少金额
						Integer residuePrice = new Integer(
								redisMapper.get(RedisConstants.KANG_RED_PACKET_PRICE + packetId).toString());
						Random random = new Random();
						// 随机范围：[1,剩余人均金额的两倍]
						price = random.nextInt((int) (residuePrice / (num + 1) * 2 - 1)) + 1;
					}
					redisMapper.decr(RedisConstants.KANG_RED_PACKET_PRICE + packetId, price);

					// 异步入库

				}

			} else {
				return new Message<>(ErrorCode.ERROR_RED_PACKET);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			redissonLock.unlock(RedisConstants.KANG_REDISSON_LOCK_PACKET + packetId);
		}
		return new Message<>(ErrorCode.SUCESS_END, price);
	}

	@Override
	public Message<?> rushRedPacket(List<Long> userIdList,Long packetId,Integer packetPrice,Integer packetNum) {
		this.resetData(packetId, packetPrice, packetNum); // 重置数据
		
		List<Integer> priceList = CollUtil.newArrayList();
		List<Long> numList = CollUtil.newArrayList();
		
		log.info("开始抢红包");
		
		for(Long userId : userIdList){ // 模拟100个用户并发抢购10个商品
			executor.submit(() -> {
				Integer price = new Integer(redisMapper.get(RedisConstants.KANG_RED_PACKET_PRICE + packetId).toString());
				if(price > 0) {
					Message<?> result = this.rushRedPacket_RedissonLock(userId, packetId);
					if(result.getCode() == 200) {
						priceList.add(new Integer(result.getData().toString()));
						numList.add(userId);
						log.info("{}用户ID:{}，{}，金额：{}分",Thread.currentThread().getName(),userId,result.getMsg(),result.getData());
					} else {
						log.info("{}用户ID:{}，{}",Thread.currentThread().getName() , userId , result.getMsg());
					}
				}else {
					log.info("{}用户ID:{}，{}",Thread.currentThread().getName() , userId , "手慢了，红包没有抢到");
				}
			});
		}
		
		try {
			Thread.sleep(5000); // 停5秒
			Integer totalPrice = 0;
			for(Integer val : priceList) {
				totalPrice = totalPrice + val;
			}
			log.info("{}个用户抢购红包成功，总金额：{}分",numList.size(),totalPrice);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return new Message<>(ErrorCode.SUCCESS);
	}

	private void resetData(Long packetId,Integer packetPrice, Integer packetNum) {
		redisMapper.set(RedisConstants.KANG_RED_PACKET_PRICE + packetId, packetPrice);
		redisMapper.set(RedisConstants.KANG_RED_PACKET_NUM+ packetId, packetNum);
	}

}
