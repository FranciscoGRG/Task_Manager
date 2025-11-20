package com.task_manager.task_service.dtos;

public record TaskEventDto(
    String type,
    Long taskId,
    Long userId,
    String userEmail,
    String title,
    String description,
    String dueDate,
    String payload
) {}
