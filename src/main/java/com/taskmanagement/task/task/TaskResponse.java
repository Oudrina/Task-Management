package com.taskmanagement.task.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskResponse {
    private String name;
    private String description;
    private String status;
    private String priority;
    private LocalDate dueDate;
}
