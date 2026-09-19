package com.keerthanaa.task_management_api.exception;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler =
            new GlobalExceptionHandler();

    @Test
    void handleTaskNotFound_shouldReturnErrorMessage() {

        TaskNotFoundException exception =
                new TaskNotFoundException(1L);

        Map<String, String> response =
                globalExceptionHandler.handleTaskNotFound(exception);

        assertEquals(
                "Task not found with id: 1",
                response.get("error")
        );
    }
}