package com.keerthanaa.task_management_api.service;

import com.keerthanaa.task_management_api.entity.Task;
import com.keerthanaa.task_management_api.exception.TaskNotFoundException;
import com.keerthanaa.task_management_api.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Page<Task> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    public Page<Task> searchTasks(String title, Pageable pageable) {

    if (title == null || title.isBlank()) {
        return taskRepository.findAll(pageable);
    }

    return taskRepository.findByTitleContainingIgnoreCase(title, pageable);
}

    public Task getTaskById(Long id) {
    return taskRepository.findById(id)
           .orElseThrow(() -> new TaskNotFoundException(id));
}

   public Task updateTask(Long id, Task updatedTask) {
    Task existingTask = taskRepository.findById(id)
            .orElseThrow(() -> new TaskNotFoundException(id));

    existingTask.setTitle(updatedTask.getTitle());
    existingTask.setDescription(updatedTask.getDescription());
    existingTask.setCompleted(updatedTask.isCompleted());

    return taskRepository.save(existingTask);
}

    public boolean deleteTask(Long id) {
    if (!taskRepository.existsById(id)) {
        return false;
    }

    taskRepository.deleteById(id);
    return true;
}
}