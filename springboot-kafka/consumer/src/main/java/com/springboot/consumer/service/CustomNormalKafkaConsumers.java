package com.springboot.consumer.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class CustomNormalKafkaConsumers {

    private static final Logger logger = LoggerFactory.getLogger(CustomNormalKafkaConsumers.class);
    public static final String DLQ_TOPIC = "dlq-topic";
    public static final String ORIGINAL_TOPIC_HEADER_KEY = "originalTopic";
    public static final String RETRY_COUNT_HEADER_KEY = "retryCount";


    @KafkaListener(topics = "#{'${spring.kafka.topics}'.split(',')}", groupId = "#{'${spring.kafka.consumer.group-id}'}", containerFactory = "kafkaListenerContainerFactory")
    public void consumeRecordsTxn(ConsumerRecord<String, String> record, Acknowledgment ack) {
        System.out.println("Current time consumer : " + System.currentTimeMillis());
        String json = record.value().toString();
        logger.info("Consuming message {}", json);
        ack.acknowledge();
    }


}
