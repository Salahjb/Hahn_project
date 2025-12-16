package com.hahn.backend.services;

import com.hahn.backend.dto.response.ProjectDto;
import com.hahn.backend.entities.Project;
import com.hahn.backend.entities.User;
import com.hahn.backend.exceptions.AccessDeniedException;
import com.hahn.backend.exceptions.ResourceNotFoundException;
import com.hahn.backend.repositories.ProjectRepository;
import com.hahn.backend.repositories.UserRepository;
import com.hahn.backend.util.EntityMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EntityMapper mapper;

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Test
    void getProjectsByUser_ShouldReturnList_WhenUserExists() {
        // 1. Arrange
        String email = "salah@test.com";
        User user = User.builder().id(1L).email(email).build();
        Project project = Project.builder().id(10L).user(user).title("Test Project").build();
        ProjectDto projectDto = ProjectDto.builder().id(10L).title("Test Project").build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(projectRepository.findByUserId(user.getId())).thenReturn(List.of(project));
        when(mapper.toProjectDto(project)).thenReturn(projectDto);

        // 2. Act
        List<ProjectDto> result = projectService.getProjectsByUser(email);

        // 3. Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Project", result.get(0).getTitle());
    }

    @Test
    void deleteProject_ShouldDelete_WhenUserIsOwner() {
        // 1. Arrange
        Long projectId = 1L;
        String email = "salah@test.com";
        User owner = User.builder().email(email).build();
        Project project = Project.builder().id(projectId).user(owner).build();

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        // 2. Act
        projectService.deleteProject(projectId, email);

        // 3. Assert
        verify(projectRepository, times(1)).delete(project);
    }

    @Test
    void deleteProject_ShouldThrowAccessDenied_WhenUserIsNotOwner() {
        // 1. Arrange
        Long projectId = 1L;
        String email = "hacker@bad.com";
        User owner = User.builder().email("owner@good.com").build(); // Different owner
        Project project = Project.builder().id(projectId).user(owner).build();

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        // 2. Act & Assert
        assertThrows(AccessDeniedException.class, () -> {
            projectService.deleteProject(projectId, email);
        });

        // Verify delete was NEVER called
        verify(projectRepository, never()).delete(any());
    }
}