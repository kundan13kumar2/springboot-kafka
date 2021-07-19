/*
package com.springboot.consumer.serdesr;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

public class KafkaAvroSerializerWithOriginalTopic extends KafkaAvroSerializer implements Serializer<Object> {


    @Override
    public byte[] serialize(String topic, Headers headers, Object data) {
        Header targetTopicHeader = headers.lastHeader("sdfs");
        if (targetTopicHeader != null) {
            return super.serialize(new String(targetTopicHeader.value()), data);
        }
        return super.serialize(topic, data);
    }
}

*/
