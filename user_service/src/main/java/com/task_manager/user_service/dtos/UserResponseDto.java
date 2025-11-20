package com.task_manager.user_service.dtos;

public record UserResponseDto(
        Long id,
        String username,
        String email) {

}
