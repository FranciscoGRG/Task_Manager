package com.task_manager.notification_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task_manager.notification_service.dtos.NotificationEventDto;
import com.task_manager.notification_service.email.EmailSender;
import com.task_manager.notification_service.models.Notification;
import com.task_manager.notification_service.repositories.NotificationRepository;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository repository;
    
    @Autowired
    private EmailSender emailSender;

    private final ObjectMapper mapper = new ObjectMapper();

    public void handleEvent(NotificationEventDto event) {
        Notification notification = new Notification();
        notification.setUserId(event.userId());
        notification.setTaskId(event.taskId());
        notification.setType(event.type());
        notification.setPayload(toJson(event));

        repository.save(notification);

        boolean sent = emailSender.send(
            event.userEmail(),
            "Tarea pendiente: " + event.title(),
            "La tarea: " + event.title() + " cumple el dia: " + event.dueDate()
        );

        if (sent) {
            notification.setSent(sent);
            repository.save(notification);
        }
    }

    private String toJson(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert to JSON", e);
        }
    }
}
