package com.hahn.backend.services;

import com.hahn.backend.dto.responce.TaskDto;
import com.hahn.backend.entities.TaskStatus;
import java.util.List;

public interface TaskService {
    // Add a task to a specific project
    TaskDto createTask(Long projectId, TaskDto request);

    // List all tasks for a project
    List<TaskDto> getTasksByProject(Long projectId);

    // Mark a task as COMPLETED or PENDING
    TaskDto updateTaskStatus(Long taskId, TaskStatus status);

    // Remove a task
    void deleteTask(Long taskId);
}