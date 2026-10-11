package org.example.inventoryservice.service;

import org.example.inventoryservice.dto.AlertMessage;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class AlertPublisher {

    public static final String CHANNEL = "pharmacy-alerts";

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public AlertPublisher(
            StringRedisTemplate redisTemplate,
            ObjectMapper objectMapper
    ) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public long publish(AlertMessage alert) {

        String json = objectMapper.writeValueAsString(alert);

        Long subscribers = redisTemplate.convertAndSend(
                CHANNEL,
                json
        );

        System.out.println(
                "Đã gửi thông báo đến kênh: " + CHANNEL
        );

        return subscribers == null ? 0 : subscribers;
    }
}
