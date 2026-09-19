package com.taskmanagement.task.task;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskRequest {
    @NotBlank(message = "The name cannot be empty")
    private String name;
    @NotBlank(message = "The description cannot be empty")
    private String description;

    @NotBlank(message = "The Status cannot be empty")
    private String status;
    @NotBlank(message = "The Priority cannot be empty")
    private String priority;

    @NotNull(message = "Due Date cannot be empty")
    private LocalDate dueDate;
}
