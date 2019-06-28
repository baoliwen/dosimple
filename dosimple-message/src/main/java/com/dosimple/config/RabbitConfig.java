package com.dosimple.config;

import com.dosimple.message.JmsChannel;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dosimple
 */
@Configuration
public class RabbitConfig {

    @Bean(value = JmsChannel.TEST_QUEUE)
    public Queue TestQueue() {
        return new Queue(JmsChannel.TEST_QUEUE);
    }

    @Bean("FANOUT_QUEUE_A")
    public Queue fanoutA() {
        return new Queue(JmsChannel.FANOUT_QUEUE_A);
    }

    @Bean("FANOUT_QUEUE_B")
    public Queue fanoutB() {
        return new Queue(JmsChannel.FANOUT_QUEUE_B);
    }

    @Bean("FANOUT_QUEUE_C")
    public Queue fanoutC() {
        return new Queue(JmsChannel.FANOUT_QUEUE_C);
    }

    @Bean("TEST_FANOUT")
    FanoutExchange fanoutExchange() {
        return new FanoutExchange(JmsChannel.TEST_FANOUT);
    }

    @Bean
    public Binding bindingExchangeWithA() {
        return BindingBuilder.bind(fanoutA()).to(fanoutExchange());
    }

    @Bean
    public Binding bindingExchangeWithB() {
        return BindingBuilder.bind(fanoutB()).to(fanoutExchange());
    }

    @Bean
    public Binding bindingExchangeWithC() {
        return BindingBuilder.bind(fanoutC()).to(fanoutExchange());
    }

    @Bean("TOPIC_QUEUE_A")
    public Queue topicA() {
        return new Queue(JmsChannel.TOPIC_QUEUE_A);
    }

    @Bean("TOPIC_QUEUE_B")
    public Queue topicB() {
        return new Queue(JmsChannel.TOPIC_QUEUE_B);
    }

    @Bean("TOPIC_QUEUE_C")
    public Queue topicC() {
        return new Queue(JmsChannel.TOPIC_QUEUE_C);
    }

    /**
     * 定义个topic交换器
     * @return
     */
    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange(JmsChannel.TEST_TOPIC);
    }

    @Bean
    public Binding bindingTopicExchangeWithA() {
        return BindingBuilder.bind(topicA()).to(topicExchange()).with("topic.msg");
    }

    @Bean
    public Binding bindingTopicExchangeWithB() {
        return BindingBuilder.bind(topicB()).to(topicExchange()).with("topic.#");
    }

    @Bean
    public Binding bindingTopicExchangeWithC() {
        return BindingBuilder.bind(topicC()).to(topicExchange()).with("topic.*.z");
    }

}

