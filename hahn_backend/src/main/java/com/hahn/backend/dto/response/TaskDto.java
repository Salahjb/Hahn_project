package com.hahn.backend.dto.response;

import com.hahn.backend.entities.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    private Long id ;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private TaskStatus status;
    private Long projectId; //  for the frontend to know which project this belongs to
}
