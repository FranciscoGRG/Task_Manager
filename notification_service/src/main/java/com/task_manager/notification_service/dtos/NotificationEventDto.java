package com.task_manager.notification_service.dtos;

public record NotificationEventDto(
    String type,
    Long taskId,
    Long userId,
    String userEmail,
    String title,
    String description,
    String dueDate) {
}
