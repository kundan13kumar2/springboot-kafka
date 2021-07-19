/*
package com.springboot.consumer.service;

import com.springboot.consumer.ProcessCallback;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.ArrayList;
import java.util.List;

public class KafkaTxnMessage implements Runnable {

    KafkaConsumerRecordsImpl kafkaConsumerImpl;

    KafkaConsumer<String, Object> kafkaConsumer;
    ProcessCallback callback;

    public KafkaTxnMessage(KafkaConsumerRecordsImpl kafkaConsumerImpl, KafkaConsumer<String, Object> kafkaConsumer, ProcessCallback callback) {
        this.kafkaConsumer = kafkaConsumer;
        this.callback = callback;
        this.kafkaConsumerImpl = kafkaConsumerImpl;
    }

    @Override
    public void run() {
        while (true) {
            List<Object> objectList = new ArrayList<>();
            synchronized (kafkaConsumer) {
                objectList = kafkaConsumerImpl.pollRecords(kafkaConsumer);
            }
            if (objectList.size() > 0)
                callback.receiveTxnMessage(objectList);
        }
    }
}
*/
