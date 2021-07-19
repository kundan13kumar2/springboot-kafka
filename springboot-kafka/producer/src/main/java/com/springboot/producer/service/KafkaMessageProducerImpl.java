package com.springboot.producer.transaction;

import com.springboot.MessageProducer;
import com.springboot.dto.ProducerProperties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.OutOfOrderSequenceException;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class KafkaMessageProducerImpl extends SendMessage implements MessageProducer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaMessageProducerImpl.class);


    //TODO: Implement local cache for this. >> Guava / Own Cache
    private static Map<String, Producer<String, Object>> runningTxn = new HashMap<>();

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
    public void sendNormalMessage(String topic, Object message, ProducerProperties properties) {
        Producer<String, Object> producer = new KafkaProducer<String, Object>(normalProducerConfig(properties));
        try {
            logger.debug("Start sending message {} to topic {} ", message, topic);
            doSend(producer, topic, message);
            logger.debug("Done sending message {} to topic {} ", message, topic);

        } catch (Exception e) {
            logger.error("Exception while sending message {} to topic {}", message, topic);
        } finally {
            producer.close();
        }

    }

    @Override
    public void sendTxnMessage(String topic, Object message, ProducerProperties properties, boolean isCommit) {
        Producer<String, Object> producer = null;
        try {
            logger.debug("Start sending message {} to topic {} ", message, topic);
            if (!runningTxn.containsKey(properties.getTxnId())) {
                producer = new KafkaProducer<String, Object>(txnProducerConfig(properties));
                producer.initTransactions();
                producer.beginTransaction();
                runningTxn.put(properties.getTxnId(), producer);
            } else {
                producer = runningTxn.get(properties.getTxnId());
            }

            doSend(producer, topic, message);

            if (isCommit) {
                producer.commitTransaction();
                producer.close();
                logger.debug("Done sending message {} to topic {} ", message, topic);
            }
        } catch (ProducerFencedException | OutOfOrderSequenceException | AuthorizationException e) {
            throw new RuntimeException(e);
        } catch (KafkaException e) {
            assert producer != null;
            producer.abortTransaction();
            producer.close();
            logger.error("Exception while sending message!!!");
        }

    }
}
