package com.hahn.backend.controllers;

import com.hahn.backend.dto.response.TaskDto;
import com.hahn.backend.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/projects/{projectId}/tasks")
    public ResponseEntity<TaskDto> createTask(
            @PathVariable Long projectId,
            @RequestBody TaskDto request,
            Principal principal
    ) {
        return ResponseEntity.ok(taskService.createTask(projectId, request, principal.getName()));
    }

    @GetMapping("/projects/{projectId}/tasks")
    public ResponseEntity<List<TaskDto>> getTasksByProject(
            @PathVariable Long projectId,
            Principal principal
    ) {
        return ResponseEntity.ok(taskService.getTasksByProject(projectId, principal.getName()));
    }

    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<TaskDto> updateTask(
            @PathVariable Long taskId,
            @RequestBody TaskDto request,
            Principal principal
    ) {
        return ResponseEntity.ok(taskService.updateTask(taskId, request, principal.getName()));
    }

    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long taskId,
            Principal principal
    ) {
        taskService.deleteTask(taskId, principal.getName());
        return ResponseEntity.noContent().build();
    }
}