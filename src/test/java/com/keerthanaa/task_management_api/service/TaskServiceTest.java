package com.keerthanaa.task_management_api.service;

import com.keerthanaa.task_management_api.entity.Task;
import com.keerthanaa.task_management_api.exception.TaskNotFoundException;
import com.keerthanaa.task_management_api.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    Pageable pageable = PageRequest.of(0, 10);
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
    void createTask_shouldThrowExceptionWhenSaveFails() {
    Task task = new Task();
    task.setTitle("Learn Spring Boot");

    when(taskRepository.save(task))
            .thenThrow(new RuntimeException("Database error"));

    assertThrows(RuntimeException.class, () -> {
        taskService.createTask(task);
    });

    verify(taskRepository, times(1)).save(task);
    }

    @Test
    void getAllTasks_shouldReturnEmptyListWhenNoTasks() {
    when(taskRepository.findAll(pageable))
            .thenReturn(Page.empty(pageable));

    Page<Task> result = taskService.getAllTasks(pageable);

    assertTrue(result.getContent().isEmpty());

    verify(taskRepository, times(1)).findAll(pageable);
    }

    @Test
    void getAllTasks_shouldReturnAllTasks() {
        Task task1 = new Task();
        task1.setTitle("Task 1");

        Task task2 = new Task();
        task2.setTitle("Task 2");

        Page<Task> taskPage = new PageImpl<>(
        List.of(task1, task2),
        pageable,
        2
        );

        when(taskRepository.findAll(pageable))
            .thenReturn(taskPage);

        Page<Task> result = taskService.getAllTasks(pageable);

        assertEquals(2, result.getContent().size());
        assertEquals("Task 1", result.getContent().get(0).getTitle());
        assertEquals("Task 2", result.getContent().get(1).getTitle());
        verify(taskRepository, times(1)).findAll(pageable);
    }

    @Test
void searchTasks_shouldReturnMatchingTasks() {

    Task task = new Task();
    task.setTitle("Practice Java");

    Page<Task> taskPage = new PageImpl<>(
            List.of(task),
            pageable,
            1
    );

    when(taskRepository.findByTitleContainingIgnoreCase(
            "Java",
            pageable
    )).thenReturn(taskPage);

    Page<Task> result = taskService.searchTasks("Java", pageable);

    assertEquals(1, result.getContent().size());
    assertEquals("Practice Java", result.getContent().get(0).getTitle());

    verify(taskRepository, times(1))
            .findByTitleContainingIgnoreCase("Java", pageable);
}

    @Test
    void getTaskById_shouldReturnTaskWhenFound() {
    Task task = new Task();
    task.setTitle("Find this task");

    when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

    Task result = taskService.getTaskById(1L);

    assertEquals("Find this task", result.getTitle());
    verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void getTaskById_shouldThrowExceptionWhenTaskNotFound() {

    when(taskRepository.findById(1L))
            .thenReturn(Optional.empty());

    assertThrows(
            TaskNotFoundException.class,
            () -> taskService.getTaskById(1L)
    );

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

    Task result =
            taskService.updateTask(1L, updatedTask);

    assertEquals("New title", result.getTitle());
    assertEquals("New description", result.getDescription());
    assertTrue(result.isCompleted());

    verify(taskRepository, times(1)).findById(1L);
    verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    void updateTask_shouldThrowExceptionWhenTaskNotFound() {
    Task updatedTask = new Task();
    updatedTask.setTitle("New title");
    updatedTask.setDescription("New description");
    updatedTask.setCompleted(true);

    when(taskRepository.findById(1L))
            .thenReturn(Optional.empty());

    assertThrows(
            TaskNotFoundException.class,
            () -> taskService.updateTask(1L, updatedTask)
    );

    verify(taskRepository, times(1)).findById(1L);
    verify(taskRepository, never()).save(any(Task.class));
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