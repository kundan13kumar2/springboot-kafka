package com.springboot.producer.service;

import com.springboot.producer.MessageProducer;
import com.springboot.producer.dto.ProducerProperties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.OutOfOrderSequenceException;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class KafkaMessageProducerImpl extends SendMessage implements MessageProducer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaMessageProducerImpl.class);


    @Autowired
    @Qualifier(value = "nonTxnTemplate")
    KafkaTemplate<String, String> nonTxnTemplate;

    @Autowired
    @Qualifier(value = "txnTemplate")
    KafkaTemplate<String, String> txnTemplate;


    //TODO: Implement local cache for this. >> Guava / Own Cache
    private static Map<String, Producer<String, String>> runningTxn = new HashMap<>();

    public Map<String, Object> txnProducerConfig(ProducerProperties properties) {
        Map<String, Object> config = super.commonProducerConfig(properties);
        config.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, properties.getTxnId());
        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, properties.getIdempotence());
        return config;
    }

    public Map<String, Object> normalProducerConfig(ProducerProperties properties) {
        return super.commonProducerConfig(properties);
    }

    @Override
    public void sendMessageSpring(String topic, String message) {
        nonTxnTemplate.send(topic, message);
    }

    @Override
    public void sendTxnMessageSpring(String topic, String message) {
        txnTemplate.executeInTransaction(t -> {
            t.send(topic, message);
            try {
                Thread.sleep(8000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            t.send(topic, message + 1);
            return null;
        });
    }

    @Override
    public void sendMessage(String topic, String message, ProducerProperties properties) {
        Producer<String, String> producer = new KafkaProducer<String, String>(normalProducerConfig(properties));
        try {
            logger.debug("Start sending message {} to topic {} ", message, topic);
            doSend(producer, topic, message);
            logger.debug("Done sending message {} to topic {} ", message, topic);
        } catch (Exception e) {
            logger.error("Exception while sending message {} to topic {}", message, topic);
            e.printStackTrace();
        }

    }

    @Override
    public void sendTxnMessage(String topic, String message, boolean isCommit, ProducerProperties properties) {
        Producer<String, String> producer = null;
        try {
            logger.debug("Start sending message {} to topic {} ", message, topic);
            if (!runningTxn.containsKey(properties.getTxnId())) {
                producer = new KafkaProducer<String, String>(txnProducerConfig(properties));
                producer.initTransactions();
                producer.beginTransaction();
                runningTxn.put(properties.getTxnId(), producer);
            } else {
                producer = runningTxn.get(properties.getTxnId());
            }

            doSend(producer, topic, message);

            if (isCommit) {
                runningTxn.remove(properties.getTxnId());
                producer.commitTransaction();
                logger.debug("Done sending message {} to topic {} ", message, topic);
            }
        } catch (ProducerFencedException | OutOfOrderSequenceException | AuthorizationException e) {
            throw new RuntimeException(e);
        } catch (KafkaException e) {
            assert producer != null;
            producer.abortTransaction();
            logger.error("Exception while sending message!!!");
        }
    }
}
