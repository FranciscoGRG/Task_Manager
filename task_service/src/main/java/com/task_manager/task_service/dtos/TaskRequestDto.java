package com.task_manager.task_service.dtos;

import java.time.LocalDateTime;

public record TaskRequestDto(
    String title,
    String description,
    LocalDateTime dueDate,
    boolean completed,
    String attachmentUrl) {
}
