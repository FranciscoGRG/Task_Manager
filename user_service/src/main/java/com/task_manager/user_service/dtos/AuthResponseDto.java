package com.task_manager.user_service.dtos;

public record AuthResponseDto(
    String token,
    String email,
    String username
) {}
