package com.kang.config.mq.rabbitmq.consumer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.kang.common.constant.RabbitConstants;
import com.rabbitmq.client.Channel;

@Component
@RabbitListener(bindings ={@QueueBinding(value = @Queue(value = RabbitConstants.MESSAGE_QUEUE,durable = "true"),
exchange =@Exchange(value = RabbitConstants.DIRECT_EXCHANGE,durable = "true"),key = RabbitConstants.MESSAGE_ROUTING_KEY)})
public class MessageConsumer_1 {

	private Logger log = LoggerFactory.getLogger(MessageConsumer_1.class);
	
	@RabbitHandler
	public void process(String jsonData,Channel channel, Message message) throws IOException{
		try {
			// 手动确认一条消息
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			log.info("Message1接受到数据为:{}" , jsonData);
		} catch (IOException e) {
			//消费者处理出了问题，需要告诉队列信息消费失败
			channel.basicNack(message.getMessageProperties().getDeliveryTag(),false, true);
			log.error("获取消息失败：{}",jsonData);
		}
		//channel.basicReject(message.getMessageProperties().getDeliveryTag(), true); 拒绝一条消息
    }
}
