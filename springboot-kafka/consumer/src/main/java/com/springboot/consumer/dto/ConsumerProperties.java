package com.springboot.consumer.dto;

import lombok.Getter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class ConsumerProperties {

    private final List<String> brokerConfig;
    private final Set<String> topics;
    private final String groupId;
    private final boolean readOnlyCommittedRecords;


    public static class Builder {
        // Required parameters
        private final List<String> brokerConfig;
        private Set<String> topics;
        private String groupId;
        private boolean readOnlyCommittedRecords;

        public Builder(List<String> brokerConfig) {
            this.brokerConfig = brokerConfig;
        }

        public Builder topics(String topic) {
            this.topics = new HashSet<>(Arrays.asList(topic.split(",")));
            return this;
        }

        public Builder groupId(String groupId) {
            this.groupId = groupId;
            return this;
        }

        public Builder readOnlyCommittedRecords(boolean readOnlyCommittedRecords) {
            this.readOnlyCommittedRecords = readOnlyCommittedRecords;
            return this;
        }

        public ConsumerProperties build() {
            return new ConsumerProperties(this);
        }
    }

    private ConsumerProperties(Builder builder) {
        brokerConfig = builder.brokerConfig;
        topics = builder.topics;
        readOnlyCommittedRecords = builder.readOnlyCommittedRecords;
        groupId = builder.groupId;
    }
}
