package com.task_manager.task_service.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.task_manager.task_service.models.Task;

@Repository
public interface ITaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(Long userId);
}
