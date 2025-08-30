package com.assignment.sportstracker.service;

import com.assignment.sportstracker.dto.ExternalApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessagePublisherService {
    private static final Logger log = LoggerFactory.getLogger(MessagePublisherService.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    public MessagePublisherService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(String topic, ExternalApiResponse response) {
        if (response == null) return;

        // keeping loop to show the retry behaviour
        for (int i = 0; i < 3; i++) {
            try {
                kafkaTemplate.send(topic, response.getEventId(), response.toString()).get();
                log.info("Published event {} to topic {}", response.getEventId(), topic);
                return;
            } catch (Exception e) {
                log.warn("Retry {}/3 - Failed to publish event {}: {}", i+1, response.getEventId(), e.getMessage());
            }
        }

        log.error("Failed to publish event {} after retries", response.getEventId());
    }
}