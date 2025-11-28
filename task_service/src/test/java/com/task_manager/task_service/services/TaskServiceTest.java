package com.task_manager.task_service.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.task_manager.task_service.clients.UserClient;
import com.task_manager.task_service.dtos.TaskEventDto;
import com.task_manager.task_service.dtos.TaskRequestDto;
import com.task_manager.task_service.dtos.TaskResponseDto;
import com.task_manager.task_service.dtos.UserDto;
import com.task_manager.task_service.models.Task;
import com.task_manager.task_service.repositories.ITaskRepository;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private ITaskRepository taskRepository;

    @Mock
    private TaskEventPublisher taskEventPublisher;

    @Mock
    private UserClient userClient;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private TaskService taskService;

    @Test
    void getTaskById_ShouldReturnTask() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskResponseDto response = taskService.getTaskById(1L);

        assertNotNull(response);
        assertEquals("Test Task", response.title());
    }

    @Test
    void getAllTasksByUserId_ShouldReturnList() {
        Task task = new Task();
        task.setId(1L);
        task.setUserId(1L);
        when(taskRepository.findByUserId(1L)).thenReturn(Collections.singletonList(task));

        List<TaskResponseDto> response = taskService.getAllTasksByUserId(1L);

        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
    }

    @Test
    void saveTask_ShouldSaveAndPublishEvent() {
        TaskRequestDto request = new TaskRequestDto("Title", "Desc", LocalDateTime.now(), false, null);

        UserDto user = new UserDto(1L, "user", "user@example.com");

        when(userClient.getUserById(anyLong(), anyString())).thenReturn(user);
        when(taskRepository.save(any(Task.class))).thenAnswer(i -> {
            Task t = (Task) i.getArguments()[0];
            t.setId(1L);
            return t;
        });

        TaskResponseDto response = taskService.saveTask(request, 1L, "token", null);

        assertNotNull(response);
        verify(taskRepository).save(any(Task.class));
        verify(taskEventPublisher).publishTaskEvent(any(TaskEventDto.class));
    }
}
