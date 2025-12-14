package com.hahn.backend.util;

import com.hahn.backend.dto.response.ProjectDto;
import com.hahn.backend.dto.response.TaskDto;
import com.hahn.backend.dto.response.UserDto;
import com.hahn.backend.entities.Project;
import com.hahn.backend.entities.Task;
import com.hahn.backend.entities.TaskStatus;
import com.hahn.backend.entities.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class EntityMapper {

    public TaskDto toTaskDto(Task task) {
        return TaskDto.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .status(task.getStatus())
                .projectId(task.getProject().getId())
                .build();
    }

    public UserDto toUserDto(User user ){
        return UserDto.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .build() ;
    }

    public ProjectDto toProjectDto(Project project) {
        // 1. Calculate the stats
        int total = project.getTasks() != null ? project.getTasks().size() : 0;

        long completedCount = project.getTasks() != null ?
                project.getTasks().stream()
                        .filter(t -> t.getStatus() == TaskStatus.COMPLETED)
                        .count() : 0;

        // Avoid division by zero!
        double progressPercentage = (total == 0) ? 0.0 : ((double) completedCount / total) * 100;

        // 2. Map the tasks (if you want them included in the project view)
        var taskDtos = project.getTasks() == null ? null :
                project.getTasks().stream()
                        .map(this::toTaskDto)
                        .collect(Collectors.toList());

        // 3. Build the DTO
        return ProjectDto.builder()
                .title(project.getTitle())
                .description(project.getDescription())
                .createdAt(project.getCreatedAt())
                .totalTasks(total)
                .completedTasks((int) completedCount)
                .progress(progressPercentage) // <--- Requirement Met Here!
                .tasks(taskDtos)
                .build();
    }
}