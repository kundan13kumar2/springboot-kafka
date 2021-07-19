package com.springboot.producer.transaction;

import com.springboot.dto.ProducerProperties;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

public abstract class SendMessage {

    private static final Logger logger = LoggerFactory.getLogger(SendMessage.class);

    protected Map<String, Object> commonProducerConfig(ProducerProperties properties) {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBrokerConfig());
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return config;
    }

    protected void doSend(Producer<String, Object> producer, String topic, Object message) {
        logger.debug("Sending message {} to topic {}", message, topic);
        producer.send(new ProducerRecord<>(topic, message), ((recordMetadata, ex) -> {
            if (ex != null) {
                logger.error("Failed to send message", ex);
                throw new RuntimeException(ex);
            } else {
                logger.trace("Sent successful message {} to topic {}", message, topic);
            }
        }
        ));
    }
}
