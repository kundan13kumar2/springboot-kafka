package com.springboot.consumer;

import com.springboot.consumer.dto.ConsumerProperties;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;

public interface KafkaConsumeRecords {

    public void poll(ConsumerProperties properties, ThreadPoolTaskExecutor threadPool, final ProcessCallback callback);

    public void retry(ConsumerProperties properties, String message);

}
