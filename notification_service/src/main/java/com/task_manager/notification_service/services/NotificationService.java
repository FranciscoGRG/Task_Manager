package com.task_manager.notification_service.services;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task_manager.notification_service.dtos.NotificationEventDto;
import com.task_manager.notification_service.dtos.NotificationRequestDto;
import com.task_manager.notification_service.dtos.NotificationResponseDto;
import com.task_manager.notification_service.email.EmailSender;
import com.task_manager.notification_service.models.Notification;
import com.task_manager.notification_service.repositories.NotificationRepository;

import jakarta.transaction.Transactional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository repository;

    @Autowired
    private EmailSender emailSender;

    private final DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

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
                "La tarea: " + event.title() + " cumple el dia: " + event.dueDate());

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

    @Transactional
    public NotificationResponseDto createAndSend(NotificationRequestDto dto) {
        Notification n = new Notification();
        n.setUserId(dto.userId());
        n.setTaskId(dto.taskId());
        n.setType(dto.type());
        n.setPayload(dto.payload());
        repository.save(n);

        boolean sent = emailSender.send(dto.userEmail(), "Notificación: " + dto.type(),
                dto.title() + " vence: " + dto.dueDate());

        if (sent) {
            n.setSent(true);
            repository.save(n);
        }

        return toDto(n);
    }

    public List<NotificationResponseDto> findByUser(Long userId) {
        return repository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private NotificationResponseDto toDto(Notification n) {
        return new NotificationResponseDto(
                n.getId(),
                n.getUserId(),
                n.getTaskId(),
                n.getType(),
                n.getPayload(),
                n.isSent(),
                n.getCreatedAt().format(fmt));
    }
}
