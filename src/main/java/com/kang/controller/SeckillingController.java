package com.kang.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kang.common.msg.Message;
import com.kang.service.RedPacketService;
import com.kang.service.SeckillingGoodsService;

/**
　 * <p>Title: SeckillingController</p> 
　 * <p>Description: 秒杀</p> 
　 * @author CK 
　 * @date 2020年4月14日
 */
@RestController
@RequestMapping(value="/goods")
public class SeckillingController {

	@Autowired
	private SeckillingGoodsService seckillingGoodsService;
	
	@Autowired
	private RedPacketService redPacketService;
	
	/**
	 *<p>Title: seckillingGoods</p> 
	 *<p>Description: </p> 
	 * @param goodsId 商品id
	 * @param goodsNum 初始化库存的数量
	 * @param userCount 初始化抢购商品的人数
	 * @return
	 */
	@RequestMapping(value="/seckillingGoods",method=RequestMethod.GET)
    public Message<?> seckillingGoods(Long goodsId,Integer goodsNum,Integer userCount){
    	return seckillingGoodsService.seckillingGoods(goodsId,goodsNum,getUseridList(userCount));
    }
	
	private List<Long> getUseridList(Integer userCount){
		List<Long> userIdList = new ArrayList<>();
		for(long i = 1; i<= userCount; i++) {
			userIdList.add(i);
		}
		return userIdList;
	}
	
	/**
	 * <p>Title: rushRedPacket</p>
	 * <p>Description: </p>
	 * @param @param userCount 初始化抢购人数
	 * @param @param packetId 红包id
	 * @param @param packetPrice 初始化红包金额
	 * @param @param packetNum 初始化红包数量
	 * @param @return
	 */
	@RequestMapping(value="/redPacket",method=RequestMethod.GET)
    public Message<?> redPacket(Integer userCount,Long packetId,Integer packetPrice,Integer packetNum){
    	return redPacketService.rushRedPacket(getUseridList(userCount), packetId, packetPrice, packetNum);
    }
}
