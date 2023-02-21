package com.security.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Kafka消息生产者
 *
 * @author fuhongxing
 */
@Slf4j
@Component
public class RedKafkaProducer {
    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    public void send(String message) {
        kafkaTemplate.send(groupId, message);
    }
}