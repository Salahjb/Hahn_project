package com.hahn.backend.dto.responce;

import com.hahn.backend.entities.TaskStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private TaskStatus status;
    private Long projectId; //  for the frontend to know which project this belongs to
}
