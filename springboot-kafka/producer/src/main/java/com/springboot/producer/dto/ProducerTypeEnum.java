package com.springboot.dto;

import lombok.Getter;

@Getter
public enum ProducerTypeEnum {

    SYNC("sync", "It sends the messages to broker in sync manner to and never keep the data at client side."),
    ASYNC("async", "It allows the producer to batching together of requests(which is great for throughput) but open " +
            "the possibility of a failure of the client machine dropping unsent data.");

    String producerType;
    String description;

    ProducerTypeEnum(String producerType, String description) {
        this.producerType = producerType;
        this.description = description;
    }
}
