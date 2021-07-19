package com.springboot;

import com.springboot.dto.ProducerProperties;

public interface MessageProducer {

    public void sendNormalMessage(String topic, Object message, ProducerProperties properties);

    public void sendTxnMessage(String topic, Object message, ProducerProperties properties, boolean isCommit);

}
