package com.hahn.backend.services;

import com.hahn.backend.dto.response.TaskDto;
import com.hahn.backend.entities.TaskStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TaskService {

    //create a task linked to the Project owner (user) ...
    TaskDto createTask(Long projectId, TaskDto request, String userEmail);

    //get the tasks linked to a given Project
    List<TaskDto> getTasksByProject(Long projectId, String userEmail);

    Page<TaskDto> getTasksByProjectWithFilters(Long projectId, String search, TaskStatus status, int page, int size, String userEmail);

    TaskDto updateTask(Long taskId, TaskDto request, String userEmail);

    void deleteTask(Long id, String userEmail);
}