package com.task_manager.task_service.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.task_manager.task_service.dtos.TaskRequestDto;
import com.task_manager.task_service.dtos.TaskResponseDto;
import com.task_manager.task_service.models.Task;
import com.task_manager.task_service.repositories.ITaskRepository;

import jakarta.transaction.Transactional;

@Service
public class TaskService {

    @Autowired
    private ITaskRepository taskRepository;

    public TaskResponseDto getTaskById(Long id) {
        return taskRepository.findById(id)
                .map(task -> new TaskResponseDto(task.getId(), task.getUserId(), task.getTitle(), task.getDescription(),
                        task.getDueDate(), task.isCompleted(), task.getAttachmentUrl()))
                .orElseThrow(() -> new RuntimeException("No se ha encontrado la tarea con el id: " + id));
    }

    public List<TaskResponseDto> getAllTasksByUserId(Long userId) {
        List<Task> tasks = taskRepository.findByUserId(userId);

        if (tasks.isEmpty()) {
            throw new RuntimeException("No se han encontrado tareas");
        }

        return tasks.stream()
                .map(task -> new TaskResponseDto(
                        task.getId(),
                        task.getUserId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getDueDate(),
                        task.isCompleted(),
                        task.getAttachmentUrl()))
                .toList();
    }

    @Transactional
    public TaskResponseDto saveTask(TaskRequestDto request, Long userId) {
        Task task = new Task();

        task.setUserId(userId);
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setDueDate(request.dueDate());
        task.setCompleted(request.completed());
        task.setAttachmentUrl(request.attachmentUrl());

        Task savedTask = taskRepository.save(task);

        return new TaskResponseDto(
                savedTask.getId(),
                savedTask.getUserId(),
                savedTask.getTitle(),
                savedTask.getDescription(),
                savedTask.getDueDate(),
                savedTask.isCompleted(),
                savedTask.getAttachmentUrl());
    }

    @Transactional
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se ha encontrado la tarea con el id: " + id));

        taskRepository.delete(task);
    }

    @Transactional
    public TaskResponseDto updateTask(Long id, TaskRequestDto request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se ha encontrado la tarea con el id: " + id));

        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setDueDate(request.dueDate());
        task.setCompleted(request.completed());
        task.setAttachmentUrl(request.attachmentUrl());

        Task updatedTask = taskRepository.save(task);

        return new TaskResponseDto(
                updatedTask.getId(),
                updatedTask.getUserId(),
                updatedTask.getTitle(),
                updatedTask.getDescription(),
                updatedTask.getDueDate(),
                updatedTask.isCompleted(),
                updatedTask.getAttachmentUrl());
    }
}
