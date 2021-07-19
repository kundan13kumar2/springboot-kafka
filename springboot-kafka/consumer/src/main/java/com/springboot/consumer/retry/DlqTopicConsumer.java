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
public class DlqTopicConsumer {
    public static final String RETRY_COUNT_HEADER_KEY = "retryCount";
    public static final String ORIGINAL_TOPIC_HEADER_KEY = "originalTopic";

    private static final Logger logger = LoggerFactory.getLogger(DlqTopicConsumer.class);

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;


    @KafkaListener(id = "dlq-topic-consumer", groupId = "dlq-topic-group", topics = "dlq-topic")
    public void consume(ConsumerRecord<?, ?> consumerRecord, Acknowledgment ack) {
        String json = consumerRecord.value().toString();
        try {
            logger.info("Consuming DLQ message {}", json);
            Header originalTopicHeader = consumerRecord.headers().lastHeader(ORIGINAL_TOPIC_HEADER_KEY);
            if (originalTopicHeader != null) {
                String originalTopic = new String(originalTopicHeader.value(), UTF_8);
                Header retryCountHeader = consumerRecord.headers().lastHeader(RETRY_COUNT_HEADER_KEY);
                int retryCount = 0;
                if (retryCountHeader != null) {
                    retryCount = Integer.parseInt(new String(retryCountHeader.value(), UTF_8));
                }
                if (retryCount < 5) {
                    retryCount += 1;
                    logger.info("Resending attempt {}", retryCount);
                    ProducerRecord<String, Object> record = new ProducerRecord<>(originalTopic, json);
                    byte[] retryCountHeaderInByte = Integer.valueOf(retryCount).toString().getBytes(UTF_8);
                    record.headers().add(RETRY_COUNT_HEADER_KEY, retryCountHeaderInByte);
                    logger.info("Waiting for 5 seconds until resend");
                    Thread.sleep(5000);
                    kafkaTemplate.send(record);

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
*/
