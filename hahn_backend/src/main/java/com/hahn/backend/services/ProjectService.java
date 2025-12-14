package com.hahn.backend.services;

import com.hahn.backend.dto.response.ProjectDto;

import java.util.List;

public interface ProjectService  {

    // Create a project linked to the logged-in user
    ProjectDto createProject(ProjectDto request, String userEmail);

    // Get all projects for the logged-in user (calculates progress %)
    List<ProjectDto> getProjectsByUser(String userEmail);

    ProjectDto updateProject(Long id, ProjectDto projectDto, String userEmail) ;

    // Get details of a single project by id ...
    ProjectDto getProjectById(Long id , String userEmail);

    public void deleteProject(Long id, String userEmail) ;
}
