package com.task_manager.task_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.task_manager.task_service.dtos.TaskEventDto;

@Service
public class TaskEventPublisher {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${notification.service.url}")
    private String notificationServiceUrl;

    public void publishTaskEvent(TaskEventDto event) {
        try {
            restTemplate.postForEntity(notificationServiceUrl, event, Void.class);
        } catch (Exception e) {
            System.err.println("Error al enviar el evento: " + e.getMessage());
        }
    }
}
