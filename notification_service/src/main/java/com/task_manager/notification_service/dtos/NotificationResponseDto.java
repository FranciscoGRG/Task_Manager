package com.task_manager.notification_service.dtos;

public record NotificationResponseDto(
    Long id,
    Long userId,
    Long taskId,
    String type,
    String payload,
    boolean sent,
    String createdAt
) {}
