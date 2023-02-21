package com.security.mq.config;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
//@EnableKafka
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String servers;
    @Value("${spring.kafka.producer.retries}")
    private int retries;
    @Value("${spring.kafka.producer.batch-size}")
    private int batchSize;
    @Value("${spring.kafka.producer.acks}")
    private int acks;
    @Value("${spring.kafka.producer.buffer-memory}")
    private int bufferMemory;

//    @Bean
//    public ProducerFactory<String, Object> producerFactory() {
//        return new DefaultKafkaProducerFactory<>(producerConfig());
//    }



//    @Bean
//    public KafkaTemplate<String, Object> kafkaTemplate() {
//        return new KafkaTemplate<String, Object>(producerFactory());
//    }

    /**
     * 通过bean创建(bean的名字为initialTopic)
     *
     * @return
     */
//    @Bean
//    public NewTopic initialTopic() {
//        return new NewTopic("test", 3, (short) 1);
//    }

    /**
     * 创建一个kafka管理类，相当于rabbitMQ的管理类rabbitAdmin,没有此bean无法自定义的使用adminClient创建topic
     *
     * @return
     */
//    @Bean
//    public KafkaAdmin kafkaAdmin() {
//        Map<String, Object> props = new HashMap<>();
//        //配置Kafka实例的连接地址
//        //kafka的地址，不是zookeeper
//        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
//        return new KafkaAdmin(props);
//    }

    @Bean
    public Properties getKafkaConfig() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        properties.put(ProducerConfig.RETRIES_CONFIG, retries);
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        properties.put(ProducerConfig.ACKS_CONFIG, acks);
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return properties;
    }

    /**
     * kafka客户端，在spring中创建这个bean之后可以注入并且创建topic,用于集群环境，创建对个副本
     *
     * @return
     */
    @Bean
    public AdminClient adminClient() {
        // 方式一：AdminClient.create(getKafkaConfig())
        // 方式二：KafkaAdminClient.create(getKafkaConfig())
        return KafkaAdminClient.create(getKafkaConfig());
    }

}
