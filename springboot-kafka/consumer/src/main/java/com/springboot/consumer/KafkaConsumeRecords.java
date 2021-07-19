package com.springboot;

import com.springboot.dto.ConsumerProperties;

import java.util.List;

public interface KafkaConsumeRecords {
    public List<Object> pollRecords(ConsumerProperties properties);
}
