package com.taskmanagement.task.task;

import org.springframework.stereotype.Service;

@Service
public class TaskMapper {
    public Task toEntity(TaskRequest  request) {

        Task task = new Task();
        task.setName(request.getName());
        task.setDescription(request.getDescription());
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.valueOf(request.getPriority()));
        task.setDueDate(request.getDueDate());
        return task;

    }

    public TaskResponse toResponse(Task  task) {
        TaskResponse response = new TaskResponse();
        response.setName(task.getName());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus().toString());
        response.setPriority(task.getPriority().toString());
        response.setDueDate(task.getDueDate());
        return response;
    }
}
