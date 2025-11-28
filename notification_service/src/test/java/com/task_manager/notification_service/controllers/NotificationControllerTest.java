package com.task_manager.notification_service.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task_manager.notification_service.dtos.NotificationEventDto;
import com.task_manager.notification_service.dtos.NotificationRequestDto;
import com.task_manager.notification_service.dtos.NotificationResponseDto;
import com.task_manager.notification_service.services.NotificationService;

@WebMvcTest(NotificationController.class)
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void receiveEvent_ShouldReturnAccepted() throws Exception {
        NotificationEventDto event = new NotificationEventDto(
                "TASK_CREATED", 1L, 100L, "user@example.com", "Task Title", "Description", "2025-01-01");

        mockMvc.perform(post("/notifications/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isAccepted());

        verify(service).handleEvent(any(NotificationEventDto.class));
    }

    @Test
    void create_ShouldReturnOk() throws Exception {
        NotificationRequestDto dto = new NotificationRequestDto(
                100L, 1L, "TASK_REMINDER", "{}", "user@example.com", "Task Title", "2025-01-01");

        NotificationResponseDto response = new NotificationResponseDto(
                1L, 1L, 100L, "TASK_REMINDER", "{}", true, "2025-01-01T10:00:00");

        when(service.createAndSend(any(NotificationRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/notifications/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void getByUser_ShouldReturnList() throws Exception {
        Long userId = 1L;
        NotificationResponseDto response = new NotificationResponseDto(
                1L, userId, 100L, "TASK_REMINDER", "{}", true, "2025-01-01T10:00:00");

        when(service.findByUser(userId)).thenReturn(Collections.singletonList(response));

        mockMvc.perform(get("/notifications/notifications/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(userId));
    }
}
