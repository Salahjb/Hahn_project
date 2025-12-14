package com.hahn.backend.services;

import com.hahn.backend.dto.response.TaskDto;

import java.util.List;

public interface TaskService {

    //create a task linked to the Project owner (user) ...
    TaskDto createTask(Long projectId, TaskDto request, String userEmail);

    //get the tasks linked to a given Project
    List<TaskDto> getTasksByProject(Long projectId, String userEmail);

    TaskDto updateTask(Long taskId, TaskDto request, String userEmail);

    void deleteTask(Long id, String userEmail);
}