package com.task_manager.task_service.dtos;

import java.time.LocalDateTime;

public record TaskResponseDto(
    Long id,
    Long userId,
    String title,
    String description,
    LocalDateTime dueDate,
    boolean completed,
    String attachmentUrl) {
}
