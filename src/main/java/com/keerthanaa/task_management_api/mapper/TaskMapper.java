package com.keerthanaa.task_management_api.mapper;

import com.keerthanaa.task_management_api.dto.TaskRequest;
import com.keerthanaa.task_management_api.dto.TaskResponse;
import com.keerthanaa.task_management_api.entity.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public Task toEntity(TaskRequest request) {

        Task task = new Task();

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCompleted(request.isCompleted());

        return task;
    }

    public TaskResponse toResponse(Task task) {

        TaskResponse response = new TaskResponse();

        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setCompleted(task.isCompleted());

        return response;
    }
}