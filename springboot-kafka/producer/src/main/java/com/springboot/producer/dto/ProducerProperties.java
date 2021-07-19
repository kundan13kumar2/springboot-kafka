package com.springboot.producer.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ProducerProperties {

    private final String txnId;

    //Client --> application.yaml
    private final List<String> brokerConfig;
    private final ProducerAckEnum producerAckEnum;
    private final long requestTimeout;
    private final ProducerTypeEnum producerTypeEnum;
    private final int retryCounts;
    private final long retryInterval;
    private final String idempotence;
    private final long transactionTimeout;

    public static class Builder {
        // Required parameters
        private final List<String> brokerConfig;
        private String txnId;
        private String idempotence;

        // Optional parameters - initialized to default values

        private ProducerAckEnum producerAckEnum = ProducerAckEnum.DEFAULT_ACK;
        private long requestTimeout = 10000;
        private ProducerTypeEnum producerTypeEnum = ProducerTypeEnum.SYNC;
        private int retryCounts = 3;
        private long retryInterval = 100;
        private long transactionTimeout = 60000;


        public Builder(List<String> brokerConfig) {
            this.brokerConfig = brokerConfig;
        }

        public Builder(List<String> brokerConfig, String txnId, String idempotence) {
            this.brokerConfig = brokerConfig;
            this.txnId = txnId;
            this.idempotence = idempotence;
        }

        public Builder transactionTimeout(long transactionTimeout) {
            this.transactionTimeout = transactionTimeout;
            return this;
        }


        public Builder txnId(String txnId) {
            this.txnId = txnId;
            return this;
        }

        public Builder idempotence(String idempotence) {
            this.idempotence = idempotence;
            return this;
        }

        public Builder producerAckEnum(ProducerAckEnum producerAckEnum) {
            this.producerAckEnum = producerAckEnum;
            return this;
        }

        public Builder requestTimeout(long requestTimeout) {
            this.requestTimeout = requestTimeout;
            return this;
        }

        public Builder producerAckEnum(ProducerTypeEnum producerTypeEnum) {
            this.producerTypeEnum = producerTypeEnum;
            return this;
        }

        public Builder retryCounts(int retryCounts) {
            this.retryCounts = retryCounts;
            return this;
        }

        public Builder retryInterval(long retryInterval) {
            this.retryInterval = retryInterval;
            return this;
        }

        public ProducerProperties build() {
            return new ProducerProperties(this);
        }
    }

    private ProducerProperties(Builder builder) {
        txnId = builder.txnId;
        idempotence = builder.idempotence;
        brokerConfig = builder.brokerConfig;
        producerAckEnum = builder.producerAckEnum;
        requestTimeout = builder.requestTimeout;
        producerTypeEnum = builder.producerTypeEnum;
        retryCounts = builder.retryCounts;
        retryInterval = builder.retryInterval;
        transactionTimeout = builder.transactionTimeout;
    }
}
