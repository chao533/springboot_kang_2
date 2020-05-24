package com.kang.service;

import java.util.List;

import com.kang.common.msg.Message;

/**
 * <p>Title: RedPacketService</p>
 * <p>Description: 抢红包<p>
 * @author ChaoKang
 * @date 2020年5月22日
 */
public interface RedPacketService {
	
	
	/**
	 * <p>Title: rushRedPacket</p>
	 * <p>Description: </p>
	 * @param @param userId 用户id
	 * @param @param packetId 红包id
	 * @param @param packetPrice 初始化红包金额
	 * @param @param packetNum 初始化红包数量
	 * @param @return
	 */
	Message<?> rushRedPacket(List<Long> userIdList,Long packetId,Integer packetPrice,Integer packetNum);

	/**
	 * <p>Title: RushRedPacket</p>
	 * <p>Description: 抢红包</p>
	 * @param @param userId 用户id
	 * @param @param packetId 红包id
	 * @param @return
	 */
	Message<?> rushRedPacket_RedissonLock(Long userId,Long packetId);
}
