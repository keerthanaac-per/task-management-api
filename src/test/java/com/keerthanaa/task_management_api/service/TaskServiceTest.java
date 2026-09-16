package com.keerthanaa.task_management_api.service;

import com.keerthanaa.task_management_api.entity.Task;
import com.keerthanaa.task_management_api.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void createTask_shouldSaveAndReturnTask() {
        Task task = new Task();
        task.setTitle("Learn Spring Boot");
        task.setDescription("Study unit testing");
        task.setCompleted(false);

        when(taskRepository.save(task)).thenReturn(task);

        Task result = taskService.createTask(task);

        assertNotNull(result);
        assertEquals("Learn Spring Boot", result.getTitle());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void getAllTasks_shouldReturnAllTasks() {
        Task task1 = new Task();
        task1.setTitle("Task 1");

        Task task2 = new Task();
        task2.setTitle("Task 2");

        when(taskRepository.findAll()).thenReturn(List.of(task1, task2));

        List<Task> result = taskService.getAllTasks();

        assertEquals(2, result.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void getTaskById_shouldReturnTaskWhenFound() {
        Task task = new Task();
        task.setTitle("Find this task");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Optional<Task> result = taskService.getTaskById(1L);

        assertTrue(result.isPresent());
        assertEquals("Find this task", result.get().getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void updateTask_shouldUpdateAndReturnTaskWhenFound() {
        Task existingTask = new Task();
        existingTask.setTitle("Old title");
        existingTask.setDescription("Old description");
        existingTask.setCompleted(false);

        Task updatedTask = new Task();
        updatedTask.setTitle("New title");
        updatedTask.setDescription("New description");
        updatedTask.setCompleted(true);

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(existingTask));

        when(taskRepository.save(existingTask))
                .thenReturn(existingTask);

        Optional<Task> result =
                taskService.updateTask(1L, updatedTask);

        assertTrue(result.isPresent());
        assertEquals("New title", result.get().getTitle());
        assertEquals("New description", result.get().getDescription());
        assertTrue(result.get().isCompleted());

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    void deleteTask_shouldDeleteWhenTaskExists() {
        when(taskRepository.existsById(1L)).thenReturn(true);

        boolean result = taskService.deleteTask(1L);

        assertTrue(result);
        verify(taskRepository, times(1)).existsById(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTask_shouldReturnFalseWhenTaskDoesNotExist() {
        when(taskRepository.existsById(1L)).thenReturn(false);

        boolean result = taskService.deleteTask(1L);

        assertFalse(result);
        verify(taskRepository, times(1)).existsById(1L);
        verify(taskRepository, never()).deleteById(1L);
    }
}