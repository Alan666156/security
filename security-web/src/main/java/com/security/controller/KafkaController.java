package com.security.controller;

import com.security.dto.KafkaTopicDto;
import com.security.mq.KafkaProducerUtils;
import com.security.mq.RedKafkaProducer;
import com.security.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Controller;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Kafka功能测试
 *
 * @author fuhongxing
 */
@Slf4j
@RestController
@RequestMapping("/kafka")
public class KafkaController {

    @Resource
    private RedKafkaProducer redKafkaProducer;
    @Resource
    private KafkaProducerUtils kafkaProducerUtils;
    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @GetMapping("/createTopic")
    public Result createTopic(@RequestBody KafkaTopicDto dto) {
        kafkaProducerUtils.createTopic(dto.getTopicName(), dto.getPartitionNum(), 1);
        return Result.success();
    }

    @GetMapping("/deleteTopic")
    public Result deleteTopic() {
        kafkaProducerUtils.deleteTopic(null);
        return Result.success();
    }

    @GetMapping("/listAllTopic")
    public Result listAllTopic() {
        return Result.success(kafkaProducerUtils.listAllTopic());
    }

    @GetMapping("/sendMessage")
    @ResponseBody
    public Result sendMessage(@RequestParam String message) {
//        redKafkaProducer.send(message);
        kafkaProducerUtils.sendMessage(groupId, message);
        return Result.success("mq");
    }


    /**
     * kafkaTemplate提供了一个回调方法addCallback，我们可以在回调方法中监控消息是否发送成功 或 失败时做补偿处理，有两种写法
     *
     * @param callbackMessage
     */
    @GetMapping("/callbackOne/{message}")
    public void sendMessage2(@PathVariable("message") String callbackMessage) {
        kafkaProducerUtils.sendMessageAndCallback("test", callbackMessage);
    }

    @GetMapping("/callbackTwo/{message}")
    public void sendMessage3(@PathVariable("message") String callbackMessage) {
        kafkaTemplate.send("topic1", callbackMessage).addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.info("发送消息失败：{}", ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                log.info("发送消息成功：{}-{}-{}" + result.getRecordMetadata().topic(), result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
            }
        });
    }
}