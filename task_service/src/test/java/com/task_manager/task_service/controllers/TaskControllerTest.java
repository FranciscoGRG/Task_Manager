package com.task_manager.task_service.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task_manager.task_service.dtos.TaskRequestDto;
import com.task_manager.task_service.dtos.TaskResponseDto;
import com.task_manager.task_service.services.FileStorageService;
import com.task_manager.task_service.services.TaskService;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private FileStorageService fileStorageService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllTasksByUserId_ShouldReturnList() throws Exception {
        TaskResponseDto task = new TaskResponseDto(1L, 1L, "Title", "Desc", LocalDateTime.now(), false, null);
        when(taskService.getAllTasksByUserId(1L)).thenReturn(Collections.singletonList(task));

        mockMvc.perform(get("/tasks")
                .requestAttr("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void createTask_ShouldReturnCreatedTask() throws Exception {
        TaskRequestDto request = new TaskRequestDto("Title", "Desc", LocalDateTime.now(), false, null);
        TaskResponseDto response = new TaskResponseDto(1L, 1L, "Title", "Desc", LocalDateTime.now(), false, null);

        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "content".getBytes());
        MockMultipartFile taskPart = new MockMultipartFile("task", "", "application/json",
                objectMapper.writeValueAsBytes(request));

        when(taskService.saveTask(any(TaskRequestDto.class), anyLong(), anyString(), any())).thenReturn(response);

        mockMvc.perform(multipart("/tasks")
                .file(file)
                .file(taskPart)
                .requestAttr("userId", 1L)
                .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }
}
