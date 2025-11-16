package com.task_manager.user_service.dtos;

public record RegisterRequestDto(
    String email,
    String username,
    String password
) {}
