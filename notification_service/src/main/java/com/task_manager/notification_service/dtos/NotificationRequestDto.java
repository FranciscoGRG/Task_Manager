package com.task_manager.notification_service.dtos;

public record NotificationRequestDto(
    Long userId,
    Long taskId,
    String type,
    String payload,
    String userEmail,
    String title,
    String dueDate
) {}
