package com.hahn.backend.services;

import com.hahn.backend.dto.response.ProjectDto;
import com.hahn.backend.entities.Project;
import com.hahn.backend.entities.User;
import com.hahn.backend.exceptions.AccessDeniedException;
import com.hahn.backend.exceptions.ResourceNotFoundException;
import com.hahn.backend.repositories.ProjectRepository;
import com.hahn.backend.repositories.UserRepository;
import com.hahn.backend.util.EntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final EntityMapper mapper;

    @Override
    @Transactional
    public ProjectDto createProject(ProjectDto request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

        Project project = Project.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .user(user)
                .build();

        Project savedProject = projectRepository.save(project);
        return mapper.toProjectDto(savedProject);
    }

    @Override
    // Read-only transaction is faster
    @Transactional(readOnly = true)
    public List<ProjectDto> getProjectsByUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

        /// Note: we can optimize this query later to fetch User+Projects in one go using a JOIN

        return projectRepository.findByUserId(user.getId()).stream()
                .map(mapper::toProjectDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectDto getProjectById(Long id, String userEmail) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        if (!project.getUser().getEmail().equals(userEmail)) {
            throw new AccessDeniedException("You are not authorized to view this project");
        }

        return mapper.toProjectDto(project);
    }

    @Override
    @Transactional
    public ProjectDto updateProject(Long id, ProjectDto projectDto, String userEmail) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        if (!project.getUser().getEmail().equals(userEmail)) {
            throw new AccessDeniedException("You are not authorized to update this project");
        }

        if (projectDto.getTitle() != null) project.setTitle(projectDto.getTitle());
        if (projectDto.getDescription() != null) project.setDescription(projectDto.getDescription());

        return mapper.toProjectDto(projectRepository.save(project));
    }

    @Override
    @Transactional
    public void deleteProject(Long id, String userEmail) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        if (!project.getUser().getEmail().equals(userEmail)) {
            throw new AccessDeniedException("You are not authorized to delete this project");
        }

        projectRepository.delete(project);
    }
}