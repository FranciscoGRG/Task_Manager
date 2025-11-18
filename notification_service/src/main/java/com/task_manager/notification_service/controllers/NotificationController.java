package com.task_manager.notification_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.task_manager.notification_service.dtos.NotificationEventDto;
import com.task_manager.notification_service.services.NotificationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/events")
public class NotificationController {

    @Autowired
    private NotificationService service;

    @PostMapping()
    public ResponseEntity<Void> receiveEvent(@RequestBody NotificationEventDto event) {
        service.handleEvent(event);
        return ResponseEntity.accepted().build();
    }
    
}
