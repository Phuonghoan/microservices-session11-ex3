package org.example.inventoryservice.subscriber;

import org.example.inventoryservice.dto.AlertMessage;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;

@Service
public class AlertSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;

    public AlertSubscriber(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {

        String json = new String(
                message.getBody(),
                StandardCharsets.UTF_8
        );

        try {
            AlertMessage alert = objectMapper.readValue(
                    json,
                    AlertMessage.class
            );

            System.out.println();
            System.out.println("=== THÔNG BÁO DASHBOARD QUẢN LÝ ===");
            System.out.println("Type: " + alert.type());
            System.out.println("Nội dung: " + alert.message());
            System.out.println("==================================");
            System.out.println();

        } catch (Exception e) {
            System.err.println(
                    "Lỗi đọc thông báo Redis: " + e.getMessage()
            );
        }
    }
}
