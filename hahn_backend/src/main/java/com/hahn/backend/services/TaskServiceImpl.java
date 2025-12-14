package com.hahn.backend.services;

import com.hahn.backend.dto.response.TaskDto;
import com.hahn.backend.entities.Project;
import com.hahn.backend.entities.Task;
import com.hahn.backend.entities.TaskStatus;
import com.hahn.backend.exceptions.ResourceNotFoundException;
import com.hahn.backend.repositories.ProjectRepository;
import com.hahn.backend.repositories.TaskRepository;
import com.hahn.backend.util.EntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final EntityMapper mapper;

    @Override
    public TaskDto createTask(Long projectId, TaskDto request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .dueDate(request.getDueDate())
                .status(TaskStatus.PENDING)
                .project(project)
                .build();

        Task savedTask = taskRepository.save(task);

        return mapper.toTaskDto(savedTask);
    }


    @Override
    public List<TaskDto> getTasksByProject(Long projectId) {

        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("Project not found with id: " + projectId);
        }
        List<Task> tasks = taskRepository.findByProjectId(projectId);

        return tasks.stream()
                .map(mapper::toTaskDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDto updateTaskStatus(Long taskId, TaskStatus status) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
        task.setStatus(status);

        Task updatedTask = taskRepository.save(task);

        return mapper.toTaskDto(updatedTask);
    }

    @Override
    public void deleteTask(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new ResourceNotFoundException("Task not found with id: " + taskId);
        }
        taskRepository.deleteById(taskId);
    }
}