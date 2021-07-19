package com.springboot.consumer;

import com.springboot.KafkaConsumeRecords;
import com.springboot.config.KafkaConsumerConfig;
import com.springboot.dto.ConsumerProperties;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class KafkaConsumerRecordsImpl implements KafkaConsumeRecords {

    @Autowired
    KafkaConsumerConfig consumerConfig;

    private final Map<String, Set<String>> subscribedTopicsMap = new HashMap<>();

    @Override
    public List<Object> pollRecords(ConsumerProperties properties) {
        KafkaConsumer<String, Object> kafkaConsumer = new KafkaConsumer<>(consumerConfig.consumerConfig(properties));

        //TopicSubscription logic
        Set<String> notSubscribedList = new HashSet<>();
        Set<String> subscribedList = subscribedTopicsMap.getOrDefault(properties.getGroupId(), new HashSet<>());
        if (subscribedList.isEmpty()) {
            kafkaConsumer.subscribe(properties.getTopics());
            subscribedTopicsMap.put(properties.getGroupId(), properties.getTopics());
        } else {
            notSubscribedList = properties.getTopics().stream().
                    filter(topic -> !subscribedList.contains(topic))
                    .collect(Collectors.toSet());
            kafkaConsumer.subscribe(notSubscribedList);
        }

        List<Object> result = new ArrayList<>();

        ConsumerRecords<String, Object> records = kafkaConsumer.poll(Duration.ofMillis(100));
        records.forEach(record -> result.add(record.value()));

        return result;

    }
}
