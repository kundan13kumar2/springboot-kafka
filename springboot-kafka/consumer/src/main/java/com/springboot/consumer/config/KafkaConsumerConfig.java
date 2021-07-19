package com.springboot.config;

import com.springboot.dto.ConsumerProperties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    public Map<String, Object> consumerConfig(ConsumerProperties properties) {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBrokerConfig());
        config.put(ConsumerConfig.GROUP_ID_CONFIG, properties.getGroupId());
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        if (properties.isReadOnlyCommittedRecords())
            config.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        else
            config.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_uncommitted");
        return config;
    }

}
