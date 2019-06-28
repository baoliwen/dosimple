package com.dosimple.controller;

import com.dosimple.message.JmsChannel;
import com.dosimple.message.JmsSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dosimple
 */
@RestController
public class MessageSendController {
    @Autowired
    private JmsSender jmsSender;

    @GetMapping("/send/queue")
    public String sendQueue() {
        jmsSender.rabbitQueue(JmsChannel.TEST_QUEUE, "hello");
        return "success";
    }

    @GetMapping("/send/fanout")
    public String sendFanout() {
        jmsSender.rabbitFanout(JmsChannel.TEST_FANOUT,"", "hello");
        return "success";
    }
    @GetMapping("/send/topic")
    public String sendTopic() {
        jmsSender.rabbitTopic(JmsChannel.TEST_TOPIC, "hello", "topic.msg");
        jmsSender.rabbitTopic(JmsChannel.TEST_TOPIC, "hello", "topic.good.msg");
        jmsSender.rabbitTopic(JmsChannel.TEST_TOPIC, "hello", "topic.m.z");
        return "success";
    }
}
