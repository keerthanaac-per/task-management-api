package com.keerthanaa.task_management_api.controller;

import com.keerthanaa.task_management_api.dto.PageResponse;
import com.keerthanaa.task_management_api.dto.TaskRequest;
import com.keerthanaa.task_management_api.dto.TaskResponse;
import com.keerthanaa.task_management_api.entity.Task;
import com.keerthanaa.task_management_api.mapper.TaskMapper;
import com.keerthanaa.task_management_api.service.TaskService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    public TaskController(
        TaskService taskService,
        TaskMapper taskMapper) {

    this.taskService = taskService;
    this.taskMapper = taskMapper;
    }
   @PostMapping
   @ResponseStatus(HttpStatus.CREATED)
   public TaskResponse createTask(
        @Valid @RequestBody TaskRequest request) {

    Task task = taskMapper.toEntity(request);

    return taskMapper.toResponse(
        taskService.createTask(task));
    }

    @GetMapping
    public PageResponse<TaskResponse> getAllTasks(
            @RequestParam(required = false) String title,
            Pageable pageable) {

    Page<Task> taskPage = taskService.searchTasks(title,pageable);

    List<TaskResponse> content = taskPage.getContent()
            .stream()
            .map(taskMapper::toResponse)
            .toList();

    return new PageResponse<>(
            content,
            taskPage.getNumber(),
            taskPage.getSize(),
            taskPage.getTotalElements(),
            taskPage.getTotalPages()
    );
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(
        @PathVariable Long id) {

    Task task = taskService.getTaskById(id);

    return ResponseEntity.ok(taskMapper.toResponse(task));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
        @PathVariable Long id,
        @Valid @RequestBody TaskRequest request) {

    Task task = taskMapper.toEntity(request);

    Task updatedTask = taskService.updateTask(id, task);

    return ResponseEntity.ok(taskMapper.toResponse(updatedTask));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {

    boolean deleted = taskService.deleteTask(id);

    if (deleted) {
        return ResponseEntity.noContent().build();
    }

    return ResponseEntity.notFound().build();
    }
}
