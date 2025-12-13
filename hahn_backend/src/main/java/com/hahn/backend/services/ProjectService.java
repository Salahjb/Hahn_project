package com.hahn.backend.services;

import com.hahn.backend.dto.responce.ProjectDto;

import java.util.List;

public interface ProjectService  {
    // Create a project linked to the logged-in user
    ProjectDto createProject(ProjectDto request, String userEmail);

    // Get all projects for the logged-in user (calculates progress %)
    List<ProjectDto> getProjectsByUser(String userEmail);

    // Get details of a single project
    ProjectDto getProjectById(Long id);

    // Delete a project (and its tasks)
    void deleteProject(Long id);
}
