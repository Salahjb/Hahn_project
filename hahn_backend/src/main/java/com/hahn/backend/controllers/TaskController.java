package com.hahn.backend.controllers;

import com.hahn.backend.dto.response.TaskDto;
import com.hahn.backend.entities.TaskStatus;
import com.hahn.backend.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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

//    @GetMapping("/projects/{projectId}/tasks")
//    public ResponseEntity<List<TaskDto>> getTasksByProject(
//            @PathVariable Long projectId,
//            Principal principal
//    ) {
//        return ResponseEntity.ok(taskService.getTasksByProject(projectId, principal.getName()));
//    }
    @GetMapping("/projects/{projectId}/tasks")
    public ResponseEntity<Page<TaskDto>> getTasksByProject(
            @PathVariable Long projectId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Principal principal
    ) {
        return ResponseEntity.ok(taskService.getTasksByProjectWithFilters(
                projectId, search, status, page, size, principal.getName()
        ));
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