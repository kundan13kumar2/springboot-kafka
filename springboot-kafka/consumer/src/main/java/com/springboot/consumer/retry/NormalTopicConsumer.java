/*
package com.springboot.producer.retry;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class NormalTopicConsumer {

    public static final String RETRY_COUNT_HEADER_KEY = "retryCount";
    public static final String ORIGINAL_TOPIC_HEADER_KEY = "originalTopic";
    public static final String DLQ_TOPIC = "dlq-topic";

    private static final Logger logger = LoggerFactory.getLogger(NormalTopicConsumer.class);

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(id = "normal-topic-consumer", topics = "normal-topic", groupId = "normal-topic-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void consume(ConsumerRecord<?, ?> consumerRecord, Acknowledgment ack) {
        String json = consumerRecord.value().toString();
        try {
            logger.info("Consuming normal message {}", json);
            // Simulating an error case
            throw new RuntimeException();
        } catch (Exception e) {
            String originalTopic = consumerRecord.topic();
            ProducerRecord<String, Object> record = new ProducerRecord<>(DLQ_TOPIC, json);
            record.headers().add(ORIGINAL_TOPIC_HEADER_KEY, originalTopic.getBytes(UTF_8));
            Header retryCount = consumerRecord.headers().lastHeader(RETRY_COUNT_HEADER_KEY);
            if (retryCount != null) {
                record.headers().add(retryCount);
            }
            kafkaTemplate.send(record);
        } finally {
            ack.acknowledge();
        }
    }
}
*/
