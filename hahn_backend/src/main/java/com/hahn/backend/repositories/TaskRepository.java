package com.hahn.backend.repositories;

import com.hahn.backend.entities.Task;
import com.hahn.backend.entities.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task>  findByProjectId(Long projectId);

    // NEW METHOD for Pagination/Search
    @Query("SELECT t FROM Task t WHERE t.project.id = :projectId " +
            "AND (:title IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:status IS NULL OR t.status = :status)")
    Page<Task> findByProjectIdAndFilters(
            @Param("projectId") Long projectId,
            @Param("title") String title,
            @Param("status") TaskStatus status,
            Pageable pageable
    );
}