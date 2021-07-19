package com.springboot.producer;

import com.springboot.producer.dto.ProducerProperties;

public interface MessageProducer {


    public void sendMessage(String topic, String message, ProducerProperties properties);

    public void sendTxnMessage(String topic, String message, boolean isCommit, ProducerProperties properties);

    public void sendMessageSpring(String topic, String message);

    public void sendTxnMessageSpring(String topic, String message);

}
