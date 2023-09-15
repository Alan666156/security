package com.security.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaFuture;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static com.alibaba.fastjson2.JSON.toJSONString;

/**
 * kafka生产者类工具类
 *
 * @author : fuhongxing
 * @date: 2022/7/2 17:28
 */

@SuppressWarnings({"unused"})
@Slf4j
@Component
public class KafkaProducerUtils {

    private static final String PUSH_MSG_LOG = "准备发送MQ消息内容：{}";

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Resource
    private AdminClient adminClient;

    /**
     * 如果没有topic，则创建一个
     *
     * @param topicName    topic名称
     * @param partitionNum 分区数量
     * @param replicaNum   :
     */
    public void createTopic(String topicName, int partitionNum, int replicaNum) {
        KafkaFuture<Set<String>> topics = adminClient.listTopics().names();
        try {
            // topic 是否存在
            if (topics.get().contains(topicName)) {
                return;
            }
            NewTopic newTopic = new NewTopic(topicName, partitionNum, (short) replicaNum);
            adminClient.createTopics(Collections.singleton(newTopic));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
//            Thread.currentThread().interrupt();
        }
    }

    /**
     * 删除topic
     *
     * @param topicList topic名称
     */
    public void deleteTopic(Collection<String> topicList) {
        adminClient.deleteTopics(topicList);
    }

    /**
     * 查询topic信息
     */
    public Set<String> listAllTopic() {
        ListTopicsResult result = adminClient.listTopics();
        KafkaFuture<Set<String>> names = result.names();
        try {
//            names.get().forEach(log::info);
            return names.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 传入topic名称，json格式字符串的消息，生产者进行发送
     *
     * @param topicName topic名称
     * @param jsonStr   消息json字符串
     * @return boolean  推送是否成功
     */
    public boolean sendMessage(String topicName, String jsonStr) {
        createTopic(topicName, 5, 5);
        log.info(PUSH_MSG_LOG, jsonStr);
        //发送消息，ProducerRecord 消息实体类，每条消息由（topic,key,value,timestamp)四元组封装。一条消息key可以为空和timestamp可以设置当前时间为默认值。
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(new ProducerRecord<>(topicName, jsonStr));
        return sendMessageAndCallback(future);
    }

    /**
     * 传入topic名称，json格式字符串数组的消息，生产者进行发送
     *
     * @param topicName topic名称
     * @param jsonStrs  消息json字符串数组
     * @return boolean  推送是否成功
     */
    public Boolean[] sendMessage(String topicName, String[] jsonStrs) {
        createTopic(topicName, 5, 5);
        int msgLength = jsonStrs.length;
        Boolean[] success = new Boolean[msgLength];
        for (int i = 0; i < msgLength; i++) {
            String jsonStr = jsonStrs[i];
            log.info(PUSH_MSG_LOG, jsonStr);
            //发送消息
            ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(new ProducerRecord<>(topicName,
                    jsonStr));
            success[i] = sendMessageAndCallback(future);
        }
        return success;
    }

    /**
     * 传入topic名称，消息对象，生产者进行发送
     *
     * @param topicName topic名称
     * @param obj       消息对象
     * @return boolean  推送是否成功
     */
    public boolean sendMessage(String topicName, Object obj) {
        createTopic(topicName, 5, 5);
        String jsonStr = toJSONString(obj);
        log.info(PUSH_MSG_LOG, jsonStr);
        //发送消息
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(new ProducerRecord<>(topicName,
                jsonStr));

        return sendMessageAndCallback(future);
    }

    /**
     * 传入topic名称，消息对象数组，生产者进行发送
     *
     * @param topicName topic名称
     * @param list      消息对象数组
     * @return boolean 推送是否成功
     */
    public Boolean[] sendMessage(String topicName, List<Object> list) {
        createTopic(topicName, 5, 5);
        Boolean[] success = new Boolean[list.size()];
        for (int i = 0; i < list.size(); i++) {
            Object obj = list.get(i);
            String jsonStr = toJSONString(obj);
            log.info(PUSH_MSG_LOG, jsonStr);
            //发送消息
            ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(new ProducerRecord<>(topicName,
                    jsonStr));
            success[i] = sendMessageAndCallback(future);
        }
        return success;
    }

    /**
     * 传入topic名称，json格式字符串的消息，生产者进行发送
     *
     * @param topicName topic名称
     * @param key       消息key
     * @param jsonStr   消息json字符串
     * @return boolean  推送是否成功
     */
    public boolean sendMessage(String topicName, String key, String jsonStr) {
        createTopic(topicName, 5, 5);
        log.info(PUSH_MSG_LOG, jsonStr);
        //发送消息
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(new ProducerRecord<>(topicName,
                key, jsonStr));

        return sendMessageAndCallback(future);
    }

    /**
     * 传入topic名称，json格式字符串数组的消息，生产者进行发送
     *
     * @param topicName topic名称
     * @param key       消息key
     * @param jsonStrs  消息json字符串数组
     * @return boolean  推送是否成功
     */
    public Boolean[] sendMessage(String topicName, String key, String[] jsonStrs) {
        createTopic(topicName, 5, 5);
        int msgLength = jsonStrs.length;
        Boolean[] success = new Boolean[msgLength];
        for (int i = 0; i < msgLength; i++) {
            String jsonStr = jsonStrs[i];
            log.info(PUSH_MSG_LOG, jsonStr);
            //发送消息
            ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(new ProducerRecord<>(topicName,
                    key, jsonStr));
            success[i] = sendMessageAndCallback(future);
        }
        return success;
    }

    /**
     * 传入topic名称，消息对象，生产者进行发送
     *
     * @param topicName topic名称
     * @param key       消息key
     * @param obj       消息对象
     * @return boolean  推送是否成功
     */
    public boolean sendMessage(String topicName, String key, Object obj) {
        createTopic(topicName, 5, 5);
        String jsonStr = toJSONString(obj);
        log.info(PUSH_MSG_LOG, jsonStr);
        //发送消息
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(new ProducerRecord<>(topicName,
                key, jsonStr));

        return sendMessageAndCallback(future);
    }

    /**
     * 传入topic名称，消息对象数组，生产者进行发送
     *
     * @param topicName topic名称
     * @param key       消息key
     * @param list      消息对象数组
     * @return boolean  推送是否成功
     */
    public Boolean[] sendMessage(String topicName, String key, List<Object> list) {
        createTopic(topicName, 5, 5);
        Boolean[] success = new Boolean[list.size()];
        for (int i = 0; i < list.size(); i++) {
            Object obj = list.get(i);
            String jsonStr = toJSONString(obj);
            log.info(PUSH_MSG_LOG, jsonStr);
            //发送消息
            ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(new ProducerRecord<>(topicName,
                    key, jsonStr));
            success[i] = sendMessageAndCallback(future);
        }
        return success;
    }

    /**
     * 传入topic名称，json格式字符串的消息，生产者进行发送
     *
     * @param topicName topic名称
     * @param partition 消息发送分区
     * @param key       消息key
     * @param jsonStr   消息json字符串
     * @return boolean 推送是否成功
     */
    public boolean sendMessage(String topicName, int partition, String key, String jsonStr) {
        createTopic(topicName, 5, 5);
        log.info(PUSH_MSG_LOG, jsonStr);
        //发送消息
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(new ProducerRecord<>(topicName,
                partition, key, jsonStr));

        return sendMessageAndCallback(future);
    }

    /**
     * 传入topic名称，json格式字符串数组的消息，生产者进行发送
     *
     * @param topicName topic名称
     * @param partition 消息发送分区
     * @param key       消息key
     * @param jsonStrs  消息json字符串数组
     * @return boolean  推送是否成功
     */
    public Boolean[] sendMessage(String topicName, int partition, String key, String[] jsonStrs) {
        createTopic(topicName, 5, 5);
        int msgLength = jsonStrs.length;
        Boolean[] success = new Boolean[msgLength];
        for (int i = 0; i < msgLength; i++) {
            String jsonStr = jsonStrs[i];
            log.info(PUSH_MSG_LOG, jsonStr);
            //发送消息
            ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(new ProducerRecord<>(topicName,
                    partition, key, jsonStr));
            success[i] = sendMessageAndCallback(future);
        }
        return success;
    }

    /**
     * 传入topic名称，消息对象，生产者进行发送
     *
     * @param topicName topic名称
     * @param partition 消息发送分区
     * @param key       消息key
     * @param obj       消息对象
     * @return boolean  推送是否成功
     */
    public boolean sendMessage(String topicName, int partition, String key, Object obj) {
        createTopic(topicName, 5, 5);
        String jsonStr = toJSONString(obj);
        log.info(PUSH_MSG_LOG, jsonStr);
        //发送消息
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(new ProducerRecord<>(topicName,
                partition, key, jsonStr));

        return sendMessageAndCallback(future);
    }

    /**
     * 传入topic名称，消息对象数组，生产者进行发送
     *
     * @param topicName topic名称
     * @param partition 消息发送分区
     * @param key       消息key
     * @param list      消息对象数组
     * @return boolean  推送是否成功
     */
    public Boolean[] sendMessage(String topicName, int partition, String key, List<Object> list) {
        createTopic(topicName, 5, 5);
        Boolean[] success = new Boolean[list.size()];
        for (int i = 0; i < list.size(); i++) {
            Object obj = list.get(i);
            String jsonStr = toJSONString(obj);
            log.info(PUSH_MSG_LOG, jsonStr);
            //发送消息
            ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(new ProducerRecord<>(
                    topicName, partition, key, jsonStr));
            success[i] = sendMessageAndCallback(future);
        }
        return success;
    }

    /**
     * 使用 KafkaTemplate 的 executeInTransaction 方法来声明事务发送
     *
     * @param topicName 名称
     * @param message   发送消息
     */
    public void sendMessageByTransaction(String topicName, String message) {
        // 声明事务：后面报错消息不会发出去
        kafkaTemplate.executeInTransaction(operations -> {
            operations.send(topicName, message);
            throw new RuntimeException("fail");
        });

        // 不声明事务：后面报错但前面消息已经发送成功了
//        kafkaTemplate.send("test", "test executeInTransaction");
//        throw new RuntimeException("fail");
    }


    /**
     * kafkaTemplate提供了一个回调方法addCallback，我们可以在回调方法中监控消息是否发送成功 或 失败时做补偿处理，有两种写法
     *
     * @param topicName 名称
     * @param message   发送消息
     */
    public void sendMessageAndCallback(String topicName, String message) {
        kafkaTemplate.send(topicName, message).addCallback(success -> {
            // 消息发送到的topic
            assert success != null;
            String topic = success.getRecordMetadata().topic();
            // 消息发送到的分区
            int partition = success.getRecordMetadata().partition();
            // 消息在分区内的offset
            long offset = success.getRecordMetadata().offset();
            log.info(PUSH_MSG_LOG, topic + "-" + partition + "-" + offset);
        }, failure -> {
            log.info(PUSH_MSG_LOG, "发送消息失败:" + failure.getMessage());
        });
    }

    /**
     * 处理消息并监听回调
     * kafkaTemplate提供了一个回调方法addCallback，
     * 我们可以在回调方法中监控消息是否发送成功 或 失败时做补偿处理
     *
     * @param future
     * @return boolean
     */
    private boolean sendMessageAndCallback(ListenableFuture<SendResult<String, Object>> future) {
        final boolean[] success = {false};
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {

            @Override
            public void onFailure(Throwable throwable) {
                //发送失败的处理
                log.info("生产者 发送消息失败 exMessage:{}", throwable.getMessage());
                success[0] = false;
            }

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                //成功的处理
                log.info("生产者 发送消息成功, topic:{}, partition:{}, offset:{}",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
                success[0] = true;
            }
        });
        return success[0];
    }
}