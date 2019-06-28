package com.dosimple.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author dosimple
 */
@Slf4j
@Component
public class RabbitHandle implements ConfirmCallback, ReturnCallback {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }
    /**
     * 接口用于实现消息发送到RabbitMQ交换器后接收ack回调。
     * @param correlationData
     * @param ack
     * @param cause
     */
    @Override
    public void confirm(@Nullable CorrelationData correlationData, boolean ack, @Nullable String cause) {
        if (ack) {
            log.info("消息发送成功: {}",  correlationData);
        } else {
            log.info("消息发送失败{}",cause);
        }
    }

    /**
     * 于实现消息发送到RabbitMQ交换器，但无相应队列与交换器绑定时的回调。
     * @param message
     * @param replyCode
     * @param replyText
     * @param exchange
     * @param routingKey
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.error("============================================================================");
        log.error("[replyCode]                               =[{}]", replyCode);
        log.error("[replyText]                               =[{}]", replyText);
        log.error("[exchange]                                =[{}]", exchange);
        log.error("[routingKey]                              =[{}]", routingKey);
        log.error("============================================================================");
        log.error(message.getMessageProperties() + " 发送失败");
    }
}
