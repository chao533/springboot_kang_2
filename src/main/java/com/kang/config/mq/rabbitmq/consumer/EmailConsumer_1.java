package com.kang.config.mq.rabbitmq.consumer;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kang.common.constant.RabbitConstants;
import com.kang.service.EmailService;
import com.rabbitmq.client.Channel;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;

@Component
@RabbitListener(queues = RabbitConstants.EMAIL_QUEUE)
public class EmailConsumer_1 {

	private Logger log = LoggerFactory.getLogger(EmailConsumer_1.class);
	
	@Autowired
	private EmailService emailService;
	
	@RabbitHandler
    public void process(String jsonData,Channel channel, Message message) throws IOException {
		try {
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			log.info("EmailConsumer接受到数据为:{}" , jsonData);
			Map<String,Object> dataMap = JSONUtil.toBean(jsonData, new TypeReference<Map<String,Object>>() {},true);
	        emailService.sendHtmlMail(MapUtil.getStr(dataMap, "to"), MapUtil.getStr(dataMap, "subject"), MapUtil.getStr(dataMap, "html"));
		} catch (IOException e) {
			//消费者处理出了问题，需要告诉队列信息消费失败
			channel.basicNack(message.getMessageProperties().getDeliveryTag(),false, true);
			log.error("获取消息失败：{}",jsonData);
		}
    	
    }
}
