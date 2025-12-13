package com.hahn.backend.dto.responce;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ProjectDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdAt;

    // Calculated Fields (Not in DB)
    private int totalTasks;
    private int completedTasks;
    private double progress; // 0.0 to 100.0

    // We can include the list of tasks inside the project detail view
    private List<TaskDto> tasks;
}
