/*
package com.springboot.producer.retry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaReciever<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaReciever.class);

  //  @KafkaListener(topics = "${kafka.topic.name}", groupId = "${kafka.consumer.group.id}")
    public void recieveData(T student) {
        LOGGER.info("Data - " + student.toString() + " recieved");
    }
}
*/
