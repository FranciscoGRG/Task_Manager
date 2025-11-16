package com.task_manager.user_service.dtos;

public record LoginRequestDto(
    String username,
    String password
) {}
