package com.dosimple.message;

import com.dosimple.dto.TestMessage;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.messaging.support.MessageBuilder;

/**
 * @author dosimple
 */
//@Component
public class ProducerApplication implements CommandLineRunner {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /*@Override
    public void run(String... args) throws Exception {
        SpringApplication.run(ProducerApplication.class, args);
    }*/

    public void run(String... args) throws Exception {
       /* // 以同步的方式发送消息，构造器构造对象消息给指定的topic
        SendResult sendResult = rocketMQTemplate.syncSend("sdfsdf"
                , MessageBuilder.withPayload("Hello, World! I'm from spring message").build());
        System.out.printf("string-topic syncSend2 sendResult=%s %n", sendResult); // 异步方式发送用户定义对象类型的消息，并实现回调接口SendCallback
        rocketMQTemplate.asyncSend("sdfsdf", new TestMessage("T_001" , "88.00"), new SendCallback() {
            // 实现消息发送成功的后续处理
            public void onSuccess(SendResult var1) {
                System.out.printf("async onSucess SendResult=%s %n", var1);
            }

            // 实现消息发送失败的后续处理
            public void onException(Throwable var1) {
                System.out.printf("async onException Throwable=%s %n", var1);
            }
        });
        // 指定topic的同时，设置tag值，以便消费端可以根据tag值进行选择性消费
        rocketMQTemplate.convertAndSend("msgExtTopic" + ":tag0", "I'm from tag0");
        // tag0 will not be consumer-selected
        rocketMQTemplate.convertAndSend("msgExtTopic" + ":tag1", "I'm from tag1");*/
        rocketMQTemplate.convertAndSend("test-topic-1", "Hello, World!");
        rocketMQTemplate.send("test-topic-1", MessageBuilder.withPayload("Hello, World! I'm from spring message").build());
        rocketMQTemplate.convertAndSend("test-topic-2", new TestMessage("sdfsd", "Dfsdf"));
    }

}
