package com.springboot.consumer.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class CustomKafkaConsumers {

    private static final Logger logger = LoggerFactory.getLogger(CustomKafkaConsumers.class);
    public static final String DLQ_TOPIC = "dlq-topic";
    public static final String ORIGINAL_TOPIC_HEADER_KEY = "originalTopic";
    public static final String RETRY_COUNT_HEADER_KEY = "retryCount";


    private final ApplicationEventPublisher publisher;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private AsyncTaskExecutor asyncTaskExecutor;

    public CustomKafkaConsumers(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }


    @KafkaListener(topics = "#{'${spring.kafka.topics}'.split(',')}", groupId = "#{'${spring.kafka.consumer.group-id}'}", containerFactory = "kafkaListenerContainerFactory")
    public void consumeRecords(ConsumerRecord<String, String> record, Acknowledgment ack) {

        String json = record.value().toString();
        try {
            logger.info("Consuming normal message {}", json);

        } catch (Exception e) {
            logger.info("Message consumption failed for message {}", json);
            String originalTopic = record.topic();
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(DLQ_TOPIC, json);
            record.headers().add(ORIGINAL_TOPIC_HEADER_KEY, originalTopic.getBytes(UTF_8));
            Header retryCount = record.headers().lastHeader(RETRY_COUNT_HEADER_KEY);
            if (retryCount != null)
                record.headers().add(retryCount);
            kafkaTemplate.send(producerRecord);
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaListener(topics = "#{'${spring.kafka.dlq.topics}'.split(',')}", groupId = "#{'${spring.kafka.dlq.consumer.group-id}'}", containerFactory = "kafkaListenerContainerFactory")
    public void dlqConsumeRecords(ConsumerRecord<String, String> consumerRecord, Acknowledgment ack) {

        String json = consumerRecord.value().toString();
        try {
            logger.info("Consuming DLQ message {}", json);
            Header originalTopicHeader = consumerRecord.headers().lastHeader(ORIGINAL_TOPIC_HEADER_KEY);
            if (originalTopicHeader != null) {
                String originalTopic = new String(originalTopicHeader.value(), UTF_8);
                Header retryCounterHeader = consumerRecord.headers().lastHeader(RETRY_COUNT_HEADER_KEY);
                int retryCount = 0;
                if (retryCounterHeader != null)
                    retryCount = Integer.parseInt(new String(retryCounterHeader.value(), UTF_8));
                if (retryCount < 5) {
                    retryCount += 1;
                    logger.info("Resending attempt {}", retryCount);
                    ProducerRecord<String, String> record = new ProducerRecord<>(originalTopic, json);
                    byte[] retryCountHeaderInByte = Integer.valueOf(retryCount).toString().getBytes(UTF_8);
                    record.headers().add(RETRY_COUNT_HEADER_KEY, retryCountHeaderInByte);
                    asyncTaskExecutor.execute(() -> {
                        try {
                            logger.info("Waiting for 5 sec until resend");
                            Thread.sleep(5000);
                            kafkaTemplate.send(record);
                        } catch (Exception e) {
                            Thread.currentThread().interrupt();
                        }
                    });
                } else {
                    logger.error("Retry limit exceeded for message {}", json);
                }
            } else {
                logger.error("Unable to resend DLQ message because it's missing the originalTopic header");
            }

        } catch (Exception e) {
            logger.error("Unable to process DLQ message {}", json);
        } finally {
            ack.acknowledge();
        }

    }


}
