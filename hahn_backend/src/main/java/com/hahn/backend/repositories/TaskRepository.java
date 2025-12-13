package com.hahn.backend.repositories;

import com.hahn.backend.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task>  findByProjectId(Long projectId);
}