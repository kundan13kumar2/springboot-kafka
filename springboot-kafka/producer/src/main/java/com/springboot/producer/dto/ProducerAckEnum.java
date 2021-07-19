package com.springboot.dto;

import lombok.Getter;

@Getter
public enum ProducerAckEnum {

    DEFAULT_ACK((short) 0, "The producer never waits for an acknowledgement from the broker."),
    LEADER_REPLICA_RECEIVE((short) 1, "The producer gets an acknowledgement after the leader replica has received the data."),
    ALL_REPLICA_RECEIVE((short) -1, "The producer gets an acknowledgement after all in-sync replicas have received the data.");

    private short ack;
    private String desc;

    ProducerAckEnum(short ack, String desc) {
        this.ack = ack;
        this.desc = desc;
    }

}
