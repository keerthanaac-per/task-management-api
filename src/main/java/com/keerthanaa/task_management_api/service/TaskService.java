package com.keerthanaa.task_management_api.service;

import com.keerthanaa.task_management_api.entity.Task;
import com.keerthanaa.task_management_api.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
    return taskRepository.findById(id);
}

    public Optional<Task> updateTask(Long id, Task updatedTask) {
    return taskRepository.findById(id)
            .map(existingTask -> {
                existingTask.setTitle(updatedTask.getTitle());
                existingTask.setDescription(updatedTask.getDescription());
                existingTask.setCompleted(updatedTask.isCompleted());

                return taskRepository.save(existingTask);
            });
}

    public boolean deleteTask(Long id) {
    if (!taskRepository.existsById(id)) {
        return false;
    }

    taskRepository.deleteById(id);
    return true;
}
}