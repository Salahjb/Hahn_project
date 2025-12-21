    package com.hahn.backend.services;

    import com.hahn.backend.dto.response.TaskDto;
    import com.hahn.backend.entities.Project;
    import com.hahn.backend.entities.Task;
    import com.hahn.backend.entities.TaskStatus;
    import com.hahn.backend.exceptions.AccessDeniedException;
    import com.hahn.backend.exceptions.ResourceNotFoundException;
    import com.hahn.backend.repositories.ProjectRepository;
    import com.hahn.backend.repositories.TaskRepository;
    import com.hahn.backend.util.EntityMapper;
    import lombok.RequiredArgsConstructor;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.domain.Sort;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import java.util.List;
    import java.util.stream.Collectors;

    @Service
    @RequiredArgsConstructor
    public class TaskServiceImpl implements TaskService {

        private final TaskRepository taskRepository;
        private final ProjectRepository projectRepository;
        private final EntityMapper mapper;

        @Override
        @Transactional
        public TaskDto createTask(Long projectId, TaskDto request, String userEmail) {
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

            // SECURITY: Only the Project Owner can add tasks
            if (!project.getUser().getEmail().equals(userEmail)) {
                throw new AccessDeniedException("You are not authorized to add tasks to this project");
            }

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
        @Transactional(readOnly = true)
        // we kept this method in case of "Export All Tasks" button that doesn't use pagination ...
        public List<TaskDto> getTasksByProject(Long projectId, String userEmail) {
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

            // SECURITY: Only owner can view tasks
            if (!project.getUser().getEmail().equals(userEmail)) {
                throw new AccessDeniedException("You are not authorized to view these tasks");
            }

            return taskRepository.findByProjectId(projectId).stream()
                    .map(mapper::toTaskDto)
                    .collect(Collectors.toList());
        }

        @Override
        @Transactional(readOnly = true)
        public Page<TaskDto> getTasksByProjectWithFilters(Long projectId, String search, TaskStatus status, int page, int size, String userEmail) {
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

            // SECURITY: Only owner can view tasks
            if (!project.getUser().getEmail().equals(userEmail)) {
                throw new AccessDeniedException("You are not authorized to view these tasks");
            }

            // Create Pageable object (Page 0, Size 5, sorted by DueDate descending)
            Pageable pageable = PageRequest.of(page, size, Sort.by("dueDate").descending());

            Page<Task> taskPage = taskRepository.findByProjectIdAndFilters(projectId, search, status, pageable);

            // Convert Page<Task> -> Page<TaskDto>
            return taskPage.map(mapper::toTaskDto);
        }

        @Override
        @Transactional
        public TaskDto updateTask(Long taskId, TaskDto request, String userEmail) {
            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

            // SECURITY: Navigation Check (Task -> Project -> User)
            if (!task.getProject().getUser().getEmail().equals(userEmail)) {
                throw new AccessDeniedException("You are not authorized to update this task");
            }

            if (request.getTitle() != null) task.setTitle(request.getTitle());
            if (request.getDescription() != null) task.setDescription(request.getDescription());
            if (request.getDueDate() != null) task.setDueDate(request.getDueDate());
            if (request.getStatus() != null) task.setStatus(request.getStatus());

            return mapper.toTaskDto(taskRepository.save(task));
        }

        @Override
        @Transactional
        public void deleteTask(Long id, String userEmail) {
            Task task = taskRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

            // SECURITY: Check ownership
            if (!task.getProject().getUser().getEmail().equals(userEmail)) {
                throw new AccessDeniedException("You are not authorized to delete this task");
            }

            taskRepository.delete(task);
        }
    }