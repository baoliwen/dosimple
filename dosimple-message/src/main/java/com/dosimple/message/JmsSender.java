package com.dosimple.message;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JmsSender {
    @Autowired
    private AmqpTemplate amqpTemplate;

    public void rabbitQueue(String queue, final String message) {
        amqpTemplate.convertAndSend(queue,message);
    }

    public void rabbitFanout(String fanoutExchange, final String message, String routeKey) {
        amqpTemplate.convertAndSend(fanoutExchange,routeKey, message);
    }

    public void rabbitTopic(String topicExchange, final String message, String routeKey) {
        amqpTemplate.convertAndSend(topicExchange,routeKey, message);
    }
}
