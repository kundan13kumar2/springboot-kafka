package com.paytm.topic.kafka.define;

import com.paytm.topic.KafkaTopicDefine;

import java.util.HashMap;
import java.util.Map;

public class KafkaTopicDefineImpl implements KafkaTopicDefine {


    public Map<String, Object> kafkaConfig = new HashMap<>();

    @Override
    public void createTopic() {

    }

    @Override
    public void modifyTopic() {

    }

    @Override
    public void deleteTopic() {

    }
}
