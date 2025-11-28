package com.task_manager.notification_service.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.task_manager.notification_service.dtos.NotificationEventDto;
import com.task_manager.notification_service.dtos.NotificationRequestDto;
import com.task_manager.notification_service.dtos.NotificationResponseDto;
import com.task_manager.notification_service.email.EmailSender;
import com.task_manager.notification_service.models.Notification;
import com.task_manager.notification_service.repositories.NotificationRepository;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository repository;

    @Mock
    private EmailSender emailSender;

    @InjectMocks
    private NotificationService service;

    @Test
    void handleEvent_ShouldSaveNotificationAndSendEmail() {
        NotificationEventDto event = new NotificationEventDto(
                "TASK_CREATED", 1L, 100L, "user@example.com", "Task Title", "Description", "2025-01-01");

        when(emailSender.send(anyString(), anyString(), anyString())).thenReturn(true);
        when(repository.save(any(Notification.class))).thenAnswer(i -> i.getArguments()[0]);

        service.handleEvent(event);

        verify(repository, times(2)).save(any(Notification.class)); // Once initially, once after sending
        verify(emailSender).send(event.userEmail(), "Tarea pendiente: " + event.title(),
                "La tarea: " + event.title() + " cumple el dia: " + event.dueDate());
    }

    @Test
    void createAndSend_ShouldSaveAndSend() {
        NotificationRequestDto dto = new NotificationRequestDto(
                100L, 1L, "TASK_REMINDER", "{}", "user@example.com", "Task Title", "2025-01-01");

        when(emailSender.send(anyString(), anyString(), anyString())).thenReturn(true);
        when(repository.save(any(Notification.class))).thenAnswer(i -> {
            Notification n = (Notification) i.getArguments()[0];
            n.setId(1L);
            n.setCreatedAt(LocalDateTime.now());
            return n;
        });

        NotificationResponseDto response = service.createAndSend(dto);

        assertNotNull(response);
        assertTrue(response.sent());
        verify(repository, times(2)).save(any(Notification.class));
    }

    @Test
    void findByUser_ShouldReturnList() {
        Long userId = 1L;
        Notification n = new Notification();
        n.setId(1L);
        n.setUserId(userId);
        n.setCreatedAt(LocalDateTime.now());

        when(repository.findByUserIdOrderByCreatedAtDesc(userId)).thenReturn(Collections.singletonList(n));

        List<NotificationResponseDto> result = service.findByUser(userId);

        assertEquals(1, result.size());
        assertEquals(userId, result.get(0).userId());
    }
}
