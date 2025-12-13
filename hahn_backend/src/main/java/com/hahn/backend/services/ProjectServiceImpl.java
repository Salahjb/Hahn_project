package com.hahn.backend.services;

import com.hahn.backend.dto.responce.ProjectDto;
import com.hahn.backend.entities.Project;
import com.hahn.backend.entities.User;
import com.hahn.backend.exceptions.ResourceNotFoundException;
import com.hahn.backend.repositories.ProjectRepository;
import com.hahn.backend.repositories.UserRepository;
import com.hahn.backend.util.EntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final EntityMapper mapper;

    @Override
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
    public List<ProjectDto> getProjectsByUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

        List<Project> projects = projectRepository.findByUserId(user.getId());

        return projects.stream()
                .map(mapper::toProjectDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectDto getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        return mapper.toProjectDto(project);
    }

    @Override
    public void deleteProject(Long id) {
        //  Check existence (Standard "Fail Fast" practice)
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project not found with id: " + id);
        }
        //  Delete (Cascade will automatically remove the Tasks!)
        projectRepository.deleteById(id);
    }
}