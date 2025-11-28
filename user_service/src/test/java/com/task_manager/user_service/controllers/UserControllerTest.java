package com.task_manager.user_service.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task_manager.user_service.dtos.AuthResponseDto;
import com.task_manager.user_service.dtos.LoginRequestDto;
import com.task_manager.user_service.dtos.RegisterRequestDto;
import com.task_manager.user_service.dtos.UserResponseDto;
import com.task_manager.user_service.services.UserService;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void register_ShouldReturnOk() throws Exception {
        RegisterRequestDto request = new RegisterRequestDto("user@example.com", "user", "password");
        AuthResponseDto response = new AuthResponseDto("token", "user@example.com", "user");

        when(userService.register(any(RegisterRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"));
    }

    @Test
    void login_ShouldReturnOk() throws Exception {
        LoginRequestDto request = new LoginRequestDto("user", "password");
        AuthResponseDto response = new AuthResponseDto("token", "user@example.com", "user");

        when(userService.login(any(LoginRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"));
    }

    @Test
    void getUserById_ShouldReturnUser() throws Exception {
        UserResponseDto response = new UserResponseDto(1L, "user", "user@example.com");

        when(userService.getUserById(anyLong())).thenReturn(response);

        mockMvc.perform(get("/auth/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user"));
    }
}
