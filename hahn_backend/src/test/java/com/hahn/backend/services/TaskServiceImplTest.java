package com.hahn.backend.services;

import com.hahn.backend.dto.response.TaskDto;
import com.hahn.backend.entities.Project;
import com.hahn.backend.entities.Task;
import com.hahn.backend.entities.User;
import com.hahn.backend.exceptions.AccessDeniedException;
import com.hahn.backend.repositories.ProjectRepository;
import com.hahn.backend.repositories.TaskRepository;
import com.hahn.backend.util.EntityMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Enables Mockito
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private EntityMapper mapper;

    @InjectMocks
    private TaskServiceImpl taskService; // Injects the mocks above into this service

    @Test
    void createTask_ShouldSucceed_WhenUserIsOwner() {
        // 1. Arrange (Prepare data)
        String userEmail = "salah@test.com";
        Long projectId = 1L;

        User user = User.builder().email(userEmail).build();
        Project project = Project.builder().id(projectId).user(user).build();

        TaskDto request = TaskDto.builder()
                .title("New Task")
                .description("Desc")
                .build();

        Task savedTask = Task.builder().id(100L).title("New Task").build();
        TaskDto expectedResponse = TaskDto.builder().id(100L).title("New Task").build();

        // Mock repository behavior
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask); // Use saveAndFlush if you changed it
        when(mapper.toTaskDto(savedTask)).thenReturn(expectedResponse);

        // 2. Act (Call the method)
        TaskDto result = taskService.createTask(projectId, request, userEmail);

        // 3. Assert (Verify results)
        assertNotNull(result);
        assertEquals(100L, result.getId());
        verify(taskRepository).save(any(Task.class)); // Verify DB was called
    }

    @Test
    void createTask_ShouldThrowException_WhenUserIsNotOwner() {
        // 1. Arrange
        String ownerEmail = "salah@test.com";
        String hackerEmail = "hacker@bad.com"; // Different user!
        Long projectId = 1L;

        User owner = User.builder().email(ownerEmail).build();
        Project project = Project.builder().id(projectId).user(owner).build();

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        // 2. Act & Assert
        assertThrows(AccessDeniedException.class, () -> {
            taskService.createTask(projectId, new TaskDto(), hackerEmail);
        });

        // Verify we NEVER saved anything to DB
        verify(taskRepository, never()).save(any());
    }
}