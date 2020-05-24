package com.kang.config.mq.rabbitmq.consumer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.kang.common.constant.RabbitConstants;
import com.rabbitmq.client.Channel;

@Component
@RabbitListener(queues = RabbitConstants.TEXT_QUEUE)
public class TextConsumer_1 {
	
	private Logger log = LoggerFactory.getLogger(TextConsumer_1.class);
	
	@RabbitHandler
	public void process(String jsonData,Channel channel, Message message) throws IOException{
		try {
			// 手动确认一条消息
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			log.info("Text1接受到数据为:{}" , jsonData);
		} catch (IOException e) {
			//消费者处理出了问题，需要告诉队列信息消费失败
			channel.basicNack(message.getMessageProperties().getDeliveryTag(),false, true);
			log.error("获取消息失败：{}",jsonData);
		}
    }

	
}
