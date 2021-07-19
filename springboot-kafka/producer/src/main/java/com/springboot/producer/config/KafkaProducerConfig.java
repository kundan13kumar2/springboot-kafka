package com.springboot.producer.config;

import com.springboot.producer.dto.ProducerAckEnum;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Component
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServer;

    private static final String READ_ONLY_COMMITTED = "read_committed";

    /**
     * ++++++++++++++++++++++++++++++++++++++++++++ Transactional Configuration!!!! ++++++++++++++++++++++++++++++++
     */

    @Bean
    public Map<String, Object> txnConfigs() {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("bootstrap.servers", bootstrapServer);
        properties.put("acks", String.valueOf(ProducerAckEnum.ALL_REPLICA_RECEIVE.getAck()));
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "my.transaction.");
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, "customClientId");

        return properties;
    }

    @Primary
    @Bean(name = "factory")
    public DefaultKafkaProducerFactory<String, String> factory() {
        DefaultKafkaProducerFactory<String, String> factory = new DefaultKafkaProducerFactory<>(txnConfigs());
        return factory;
    }

    @Bean(name = "txnTemplate")
    public KafkaTemplate<String, String> template() {
        return new KafkaTemplate<>(factory());
    }


    /**
     * ++++++++++++++++++++++++++++++++++++++++++++Non Transactional Configuration!!!! ++++++++++++++++++++++++++++++++
     */

    @Bean
    public Map<String, Object> nonTxnConfigs() {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("bootstrap.servers", bootstrapServer);
        properties.put("acks", String.valueOf(ProducerAckEnum.ALL_REPLICA_RECEIVE.getAck()));
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return properties;
    }


    @Bean(name = "nonTxnFactory")
    public DefaultKafkaProducerFactory<String, String> nonTxnFactory() {
        DefaultKafkaProducerFactory<String, String> factory = new DefaultKafkaProducerFactory<>(nonTxnConfigs());
        return factory;
    }

    @Primary
    @Bean(name = "nonTxnTemplate")
    public KafkaTemplate<String, String> nonTxnTemplate() {
        return new KafkaTemplate<>(nonTxnFactory());
    }

}
