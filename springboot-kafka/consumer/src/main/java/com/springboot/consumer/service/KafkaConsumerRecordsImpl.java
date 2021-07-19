/*
package com.springboot.consumer.service;

import com.springboot.consumer.KafkaConsumeRecords;
import com.springboot.consumer.ProcessCallback;
import com.springboot.consumer.dto.ConsumerProperties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Component
public class KafkaConsumerRecordsImpl implements KafkaConsumeRecords {

    private final Map<String, Set<String>> subscribedTopicsMap = new HashMap<>();
    private final Map<String, KafkaConsumer<String, Object>> kafkaConsumerMapping = new HashMap<>();

    public static final String DLQ_TOPIC = "dlq-topic";
    public static final String ORIGINAL_TOPIC_HEADER_KEY = "originalTopic";

    @Autowired
    @Qualifier("consumerKafkaTemplate")
    private KafkaTemplate<String, String> consumerKafkaTemplate;


    public Map<String, Object> consumerConfig(ConsumerProperties properties) {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBrokerConfig());
        config.put(ConsumerConfig.GROUP_ID_CONFIG, properties.getGroupId());
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");

        return config;
    }


    public KafkaConsumer<String, Object> subscribeTopic(ConsumerProperties properties) {
        KafkaConsumer<String, Object> kafkaConsumer;

        //TopicSubscription logic start
        Set<String> notSubscribedList = new HashSet<>();
        Set<String> subscribedList = subscribedTopicsMap.getOrDefault(properties.getGroupId(), new HashSet<>());
        if (subscribedList.isEmpty()) {
            kafkaConsumer = new KafkaConsumer<>(this.consumerConfig(properties));

            kafkaConsumer.subscribe(properties.getTopics());
            subscribedTopicsMap.put(properties.getGroupId(), properties.getTopics());
            kafkaConsumerMapping.put(properties.getGroupId(), kafkaConsumer);

        } else {
            kafkaConsumer = kafkaConsumerMapping.get(properties.getGroupId());
            notSubscribedList = properties.getTopics().stream().
                    filter(topic -> !subscribedList.contains(topic))
                    .collect(Collectors.toSet());

            kafkaConsumer.subscribe(notSubscribedList);

        }

        return kafkaConsumer;
    }

    @Override
    public void poll(ConsumerProperties properties, ThreadPoolTaskExecutor threadPool, final ProcessCallback callback) {
        KafkaConsumer<String, Object> kafkaConsumer = subscribeTopic(properties);

        */
/*threadPool.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    List<Object> objectList = new ArrayList<>();
                    synchronized (kafkaConsumer) {
                  //      objectList = pollRecords(kafkaConsumer);
                    }
                    if (objectList.size() > 0)
                     //   callback.receiveMessage(objectList);
                }
            }
        });*//*

    }

    @Override
    public void retry(ConsumerProperties properties, String messages) {
        String originalTopic = properties.getTopics().toString();
        ProducerRecord<String, String> record = new ProducerRecord<>(DLQ_TOPIC, messages);
        record.headers().add(ORIGINAL_TOPIC_HEADER_KEY, originalTopic.getBytes(StandardCharsets.UTF_8));
       // consumerKafkaTemplate.send(record);

    }


    public List<Object> pollRecords(KafkaConsumer<String, Object> kafkaConsumer) {
        List<Object> result = new ArrayList<>();

        ConsumerRecords<String, Object> records = kafkaConsumer.poll(Duration.ofMillis(100));
        records.forEach(record -> {
            result.add(record.value());
        });
        kafkaConsumer.commitSync();
        return result;

    }
}
*/
