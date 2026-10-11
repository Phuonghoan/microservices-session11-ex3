package org.example.inventoryservice.controller;

import org.example.inventoryservice.dto.AlertMessage;
import org.example.inventoryservice.service.AlertPublisher;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/alerts")
public class AlertController {

    private final AlertPublisher alertPublisher;

    public AlertController(AlertPublisher alertPublisher) {
        this.alertPublisher = alertPublisher;
    }

    @PostMapping
    public ResponseEntity<String> sendAlert(
            @RequestBody AlertRequest request
    ) {

        if (request.topicListen() != null
                && !AlertPublisher.CHANNEL.equals(request.topicListen())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Channel phải là pharmacy-alerts"
            );
        }

        if (request.type() == null || request.type().isBlank()
                || request.message() == null || request.message().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Type và message không được để trống"
            );
        }

        AlertMessage alert = new AlertMessage(
                request.type(),
                request.message()
        );

        alertPublisher.publish(alert);

        return ResponseEntity.ok(
                "Đã gửi thông báo đến kênh: pharmacy-alerts"
        );
    }

    public record AlertRequest(
            String topicListen,
            String type,
            String message
    ) {
    }
}
