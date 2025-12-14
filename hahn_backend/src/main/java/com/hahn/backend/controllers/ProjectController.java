package com.hahn.backend.controllers;

import com.hahn.backend.dto.response.ProjectDto;
import com.hahn.backend.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectDto> createProject(
            @RequestBody ProjectDto request,
            Principal principal // <--- Spring automatically fills this
    ) {
        return ResponseEntity.ok(projectService.createProject(request, principal.getName()));
    }

    @GetMapping
    public ResponseEntity<List<ProjectDto>> getMyProjects(Principal principal) {
        return ResponseEntity.ok(projectService.getProjectsByUser(principal.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> getProjectById(
            @PathVariable Long id,
            Principal principal
    ) {
        return ResponseEntity.ok(projectService.getProjectById(id, principal.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDto> updateProject(
            @PathVariable Long id,
            @RequestBody ProjectDto request,
            Principal principal
    ) {
        return ResponseEntity.ok(projectService.updateProject(id, request, principal.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(
            @PathVariable Long id,
            Principal principal
    ) {
        projectService.deleteProject(id, principal.getName());
        return ResponseEntity.noContent().build(); // Returns HTTP 204 (Success, No Body)
    }
}