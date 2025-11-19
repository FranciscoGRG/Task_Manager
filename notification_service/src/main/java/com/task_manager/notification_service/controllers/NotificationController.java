package com.task_manager.notification_service.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.task_manager.notification_service.dtos.NotificationEventDto;
import com.task_manager.notification_service.dtos.NotificationRequestDto;
import com.task_manager.notification_service.dtos.NotificationResponseDto;
import com.task_manager.notification_service.services.NotificationService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService service;

    @PostMapping("/events")
    public ResponseEntity<Void> receiveEvent(@RequestBody NotificationEventDto event) {
        service.handleEvent(event);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/send")
    public ResponseEntity<NotificationResponseDto> create(@RequestBody NotificationRequestDto dto) {
        NotificationResponseDto res = service.createAndSend(dto);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/notifications/user/{userId}")
    public ResponseEntity<List<NotificationResponseDto>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(service.findByUser(userId));
    }

    @PostMapping("/prueba")
    public String sendTestMail() {
        NotificationEventDto event = new NotificationEventDto(
                "TASK_CREATED",
                99L,
                1L,
                "test@example.com",
                "Mail de prueba",
                "Este es un correo de prueba enviado desde el servicio de notificaciones.",
                "2025-01-01");

        service.handleEvent(event);

        return "Correo de prueba enviado";
    }

}
