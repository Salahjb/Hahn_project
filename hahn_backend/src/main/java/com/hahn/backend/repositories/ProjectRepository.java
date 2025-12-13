package com.hahn.backend.repositories;

import com.hahn.backend.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByUserId(Long userId);
    Optional<Project>  findProjectById(Long projectId);
}