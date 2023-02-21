package com.security.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

/**
 * Kafka消息消费者
 *
 * @author fuhongxing
 */
@Slf4j
@Component
public class RedKafkaConsumer {

    @KafkaListener(topics = "${spring.kafka.consumer.group-id}")
    public void processMessage(String content) {
        log.info("kafka consumer Message : {}", content);
    }

    /**
     * 同时监听topic1和topic2，监听topic1的0号分区、topic2的 "0号和1号" 分区，指向1号分区的offset初始值为8
     *
     ① id：消费者ID；
     ② groupId：消费组ID；
     ③ topics：监听的topic，可监听多个；
     ④ topicPartitions：可配置更加详细的监听信息，可指定topic、parition、offset监听
     **/
//    @KafkaListener(id = "consumer1", groupId = "${spring.kafka.consumer.group-id}", topicPartitions = {
//            @TopicPartition(topic = "topic1", partitions = {"0"}),
//            @TopicPartition(topic = "topic2", partitions = "0", partitionOffsets = @PartitionOffset(partition = "1", initialOffset = "8"))
//    })
//    public void onMessage2(ConsumerRecord<?, ?> record) {
//        System.out.println("topic:" + record.topic() + "|partition:" + record.partition() + "|offset:" + record.offset() + "|value:" + record.value());
//    }

}