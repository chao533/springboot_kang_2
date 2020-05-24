package com.kang.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.stereotype.Service;

import com.kang.common.constant.RabbitConstants;
import com.kang.service.RabbitmqProducerService;

import cn.hutool.core.util.IdUtil;

/**
　 * <p>Title: RabbitmqProducerServiceImpl</p> 
　 * <p>Description: 消息生产方</p> 
　 * @author CK 
　 * @date 2020年4月9日
 */
@Service
public class RabbitmqProducerServiceImpl implements RabbitmqProducerService,ConfirmCallback,ReturnCallback{

	private Logger log = LoggerFactory.getLogger(RabbitmqProducerServiceImpl.class);

	@Resource
	private RabbitTemplate rabbitTemplate;
	
	@Override
	public void sendEmail(Object jsonObj) {
		send(jsonObj, RabbitConstants.EMAIL_QUEUE);
	}

	@Override
	public void sendMessage(Object jsonObj) {
		send(jsonObj, RabbitConstants.DIRECT_EXCHANGE, RabbitConstants.MESSAGE_ROUTING_KEY);
	}
	
	private void send(Object jsonObj, String exchange, String routingKey) {
		log.info("开始发送消息:{}", jsonObj);
		//消息发送失败返回到队列中, yml需要配置 publisher-returns: true
		rabbitTemplate.setMandatory(true);
		//消息消费者确认收到消息后，手动ack回执
		rabbitTemplate.setConfirmCallback(this);
		rabbitTemplate.setReturnCallback(this);
		//发送消息
		CorrelationData correlationId = new CorrelationData(IdUtil.randomUUID().toUpperCase());
		rabbitTemplate.convertAndSend(exchange,routingKey,jsonObj,correlationId);
		log.info("消息发送结束:{}", jsonObj);
	}
	
	private void send(Object jsonObj, String routingKey) {
		log.info("开始发送消息:{}", jsonObj);
		CorrelationData correlationId = new CorrelationData(IdUtil.randomUUID().toUpperCase());
		rabbitTemplate.convertAndSend(routingKey, jsonObj, correlationId);
		log.info("消息发送结束:{}", jsonObj);
	}

	@Override
	public void sendText(Object jsonObj) {
		send(jsonObj, RabbitConstants.TOPIC_EXCHANGE, RabbitConstants.TEXT_TOPIC_KEY);
	}

	@Override
	public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
		 String msg = new String(message.getBody());
         log.error("消息:{} 发送失败, 应答码:{} 原因:{} 交换机:{} 路由键:{}", msg, replyCode, replyText, exchange, routingKey);
	}

	@Override
	public void confirm(CorrelationData correlationData, boolean ack, String cause) {
		if (ack) {
            log.info("交换机接收信息成功,id:{}", correlationData.getId());
        } else {
            log.error("交换机接收信息失败:{}", cause);
        }
		
	}
}
