
package com.keerthanaa.task_management_api.controller;

import com.keerthanaa.task_management_api.entity.Task;
import com.keerthanaa.task_management_api.exception.TaskNotFoundException;
import com.keerthanaa.task_management_api.service.TaskService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Test
    void getAllTasks_shouldReturnTasks() throws Exception {

        Task task1 = new Task();
        task1.setTitle("Learn Spring Boot");

        Task task2 = new Task();
        task2.setTitle("Learn Testing");

        when(taskService.getAllTasks())
                .thenReturn(List.of(task1, task2));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title")
                        .value("Learn Spring Boot"))
                .andExpect(jsonPath("$[1].title")
                        .value("Learn Testing"));

        verify(taskService, times(1)).getAllTasks();
    }

    @Test
    void getTaskById_shouldReturnTaskWhenFound() throws Exception {

    Task task = new Task();
    task.setTitle("Learn MockMvc");
    task.setDescription("Practice controller testing");
    task.setCompleted(false);

    when(taskService.getTaskById(1L))
            .thenReturn(task);

    mockMvc.perform(get("/api/tasks/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title")
                    .value("Learn MockMvc"))
            .andExpect(jsonPath("$.description")
                    .value("Practice controller testing"))
            .andExpect(jsonPath("$.completed")
                    .value(false));

    verify(taskService, times(1)).getTaskById(1L);
}

@Test
void getTaskById_shouldReturnNotFoundWhenTaskDoesNotExist() throws Exception {

    when(taskService.getTaskById(1L))
            .thenThrow(new TaskNotFoundException(1L));

    mockMvc.perform(get("/api/tasks/1"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error")
                    .value("Task not found with id: 1"));

    verify(taskService, times(1)).getTaskById(1L);
}

@Test
void createTask_shouldReturnCreatedTask() throws Exception {

    Task task = new Task();
    task.setId(1L);
    task.setTitle("Learn Spring Boot");
    task.setDescription("Build REST APIs");
    task.setCompleted(false);

    when(taskService.createTask(any(Task.class)))
            .thenReturn(task);

    mockMvc.perform(post("/api/tasks")
                    .contentType("application/json")
                    .content("""
                            {
                                "title": "Learn Spring Boot",
                                "description": "Build REST APIs",
                                "completed": false
                            }
                            """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title")
                    .value("Learn Spring Boot"))
            .andExpect(jsonPath("$.description")
                    .value("Build REST APIs"))
            .andExpect(jsonPath("$.completed")
                    .value(false));

    verify(taskService, times(1))
            .createTask(any(Task.class));
}

@Test
void createTask_shouldReturnBadRequestWhenTitleIsBlank() throws Exception {

    mockMvc.perform(post("/api/tasks")
                    .contentType("application/json")
                    .content("""
                            {
                                "title": "",
                                "description": "Build REST APIs",
                                "completed": false
                            }
                            """))
            .andExpect(status().isBadRequest());

    verify(taskService, never())
            .createTask(any(Task.class));
}

@Test
void deleteTask_shouldReturnNoContentWhenTaskExists() throws Exception {

    when(taskService.deleteTask(1L))
            .thenReturn(true);

    mockMvc.perform(delete("/api/tasks/1"))
            .andExpect(status().isNoContent());

    verify(taskService, times(1)).deleteTask(1L);
}

@Test
void deleteTask_shouldReturnNotFoundWhenTaskDoesNotExist() throws Exception {

    when(taskService.deleteTask(1L))
            .thenReturn(false);

    mockMvc.perform(delete("/api/tasks/1"))
            .andExpect(status().isNotFound());

    verify(taskService, times(1)).deleteTask(1L);
}

@Test
void updateTask_shouldReturnUpdatedTaskWhenTaskExists() throws Exception {

    Task updatedTask = new Task();
    updatedTask.setId(1L);
    updatedTask.setTitle("Updated Task");
    updatedTask.setDescription("Updated description");
    updatedTask.setCompleted(true);

    when(taskService.updateTask(eq(1L), any(Task.class)))
            .thenReturn(updatedTask);

    mockMvc.perform(put("/api/tasks/1")
                    .contentType("application/json")
                    .content("""
                            {
                                "title": "Updated Task",
                                "description": "Updated description",
                                "completed": true
                            }
                            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title")
                    .value("Updated Task"))
            .andExpect(jsonPath("$.description")
                    .value("Updated description"))
            .andExpect(jsonPath("$.completed")
                    .value(true));

    verify(taskService, times(1))
            .updateTask(eq(1L), any(Task.class));
}

@Test
void updateTask_shouldReturnNotFoundWhenTaskDoesNotExist() throws Exception {

    when(taskService.updateTask(eq(1L), any(Task.class)))
            .thenThrow(new TaskNotFoundException(1L));

    mockMvc.perform(put("/api/tasks/1")
                    .contentType("application/json")
                    .content("""
                            {
                                "title": "Updated Task",
                                "description": "Updated description",
                                "completed": true
                            }
                            """))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error")
                    .value("Task not found with id: 1"));

    verify(taskService, times(1))
            .updateTask(eq(1L), any(Task.class));
}
}