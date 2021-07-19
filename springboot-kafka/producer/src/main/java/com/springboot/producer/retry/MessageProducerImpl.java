/*

package com.springboot.producer.retry;

import com.springboot.producer.dto.ProducerProperties;
import com.springboot.producer.MessageProducer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessageProducerImpl {

    private static final Logger logger = LoggerFactory.getLogger(MessageProducerImpl.class);

    private static Map<String, KafkaProducer<String, Object>> kafkaProducerMap = new HashMap<>();
    private static List<String> begunTxnIds = new ArrayList<>();

    @Autowired
    private ProducerConfig producerConfig;

    @Override
    public void sendMessage(String topic, Object message, ProducerProperties properties) {
        KafkaTemplate<String, Object> template = producerConfig.getKafkaTemplate(properties);
        logger.info("Sending normal message {}: ", message);
        template.send(topic, message);

    }

    //DB Operation Rollback
    @Override
    public void sendTxnMessage(String topic, Object message, ProducerProperties properties, boolean commit) {
        Map<String, Object> kafkaProducerConfig = producerConfig.producerConfig(properties);
        kafkaProducerConfig.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, properties.getTxnId());
        kafkaProducerConfig.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, properties.getIdempotence());
        KafkaProducer<String, Object> producer = null;
        try {

            ProducerRecord<String, Object> record = new ProducerRecord<>(topic, message);
            if (!begunTxnIds.contains(properties.getTxnId())) {
                producer = new KafkaProducer<String, Object>(kafkaProducerConfig);
                producer.initTransactions();
                producer.beginTransaction();
                begunTxnIds.add(properties.getTxnId());
                kafkaProducerMap.put(properties.getTxnId(), producer);
            } else producer = kafkaProducerMap.get(properties.getTxnId());

            logger.info("Sending txn message {}: ", message);
            producer.send(record);
        } catch (Exception e) {
            producer.abortTransaction();
            e.printStackTrace();
            logger.error("Error while sending transactional message.");
        }
        if (commit) {
            Map<TopicPartition, OffsetAndMetadata> groupCommit = new HashMap<TopicPartition, OffsetAndMetadata>() {
                {
                    put(new TopicPartition(topic, 0), new OffsetAndMetadata(42L, null));
                }
            };
            producer.sendOffsetsToTransaction(groupCommit, "normal-topic-group");
            logger.info("Committing txn message {}: ", message);
            producer.commitTransaction();
        }
    }

}

*/
