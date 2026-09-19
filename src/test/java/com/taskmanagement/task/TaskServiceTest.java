package com.taskmanagement.task;

import com.taskmanagement.task.exception.TaskNotFoundException;
import com.taskmanagement.task.task.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    TaskRepository taskRepository;

    @Mock
    TaskMapper taskMapper;


    @InjectMocks
    TaskService taskService;

    private Task task;
    private TaskRequest taskRequest;
    private TaskResponse taskResponse;

    @BeforeEach
    public void setup() {
        task = new Task();
        task.setId(1L);
        task.setName("Task1");
        task.setDescription("Task1 description");
        task.setStatus(TaskStatus.COMPLETED);
        task.setPriority(TaskPriority.valueOf("HIGH"));
        task.setDueDate(LocalDate.of(2026,9,30));

        taskRequest = new TaskRequest();
        taskRequest.setName("Task1");
        taskRequest.setDescription("Task1 description");
        taskRequest.setStatus(String.valueOf(TaskStatus.COMPLETED));
        taskRequest.setPriority("HIGH");
        taskRequest.setDueDate(LocalDate.of(2026, 9, 30));

        taskResponse = new TaskResponse();
        taskResponse.setName("Task1");
        taskResponse.setDescription("Task1 description");
        taskResponse.setStatus("COMPLETED");
        taskResponse.setPriority("HIGH");
        taskResponse.setDueDate(LocalDate.of(2026, 9, 30));



    }

    @Test
    public void getAllTasks() {
        when(taskRepository.findAll()).thenReturn(Arrays.asList(task));

        taskService.getTasks();

        verify(taskRepository).findAll();
    }


    @Test
    public void createTask_shouldReturnTaskResponse() {

//        Arrange
      when(taskMapper.toEntity(taskRequest)).thenReturn(task);
      when(taskRepository.save(task)).thenReturn(task);
     when(taskMapper.toResponse(task)).thenReturn(taskResponse);

//     Act
      TaskResponse result= taskService.createTask(taskRequest);

//        Assert
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(taskResponse);
        verify(taskRepository).save(task);

    }


    @Test
    void shouldGetTaskById_whenTaskIsFound()
    {
    when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
    when(taskMapper.toResponse(task)).thenReturn(taskResponse);

//    Act

        TaskResponse result = taskService.getTaskById(task.getId());


        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(taskResponse);
        verify(taskRepository).findById(task.getId());


    }
    @Test
    void shouldThrowException_whenTaskIsNotFound() {
        Long nonExistentId = 4L;
        when(taskRepository.findById(nonExistentId)).thenReturn(Optional.empty());

//        Act and Assert
        assertThatThrownBy(()-> taskService.getTaskById(nonExistentId))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining(" not found");
    }


    @Test
    void updateTask_shouldReturnTaskResponse() {


        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        when(taskMapper.toResponse(task)).thenReturn(taskResponse);

        TaskResponse result= taskService.updateTask(task.getId(), taskRequest);

        assertThat(task.getName()).isEqualTo(taskRequest.getName());
        assertThat(task.getDescription()).isEqualTo(taskRequest.getDescription());
        assertThat(task.getStatus()).isEqualTo( TaskStatus.valueOf(taskRequest.getStatus()));
        assertThat(task.getPriority()).isEqualTo(TaskPriority.valueOf(taskRequest.getPriority()));
        assertThat(task.getDueDate()).isEqualTo(taskRequest.getDueDate());

        assertThat(result).isEqualTo(taskResponse);
        verify(taskRepository).findById(task.getId());

    }

    @Test
    void updateTask_shouldThrowException_whenTaskIsNotFound() {
        Long nonExistentId = 4L;
        when(taskRepository.findById(nonExistentId)).thenReturn(Optional.empty());
        assertThatThrownBy(()-> taskService.updateTask(nonExistentId, taskRequest))
        .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining("not found");

    }

    @Test
    void deleteTask_shouldDeleteTask() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
       taskService.deleteTask(task.getId());

       verify(taskRepository).delete(task);
    }

    @Test
    void deleteTask_shouldThrowException_whenTaskIsNotFound() {

        Long nonExistentId = 4L;
        when(taskRepository.findById(nonExistentId)).thenReturn(Optional.empty());
        assertThatThrownBy(()-> taskService.deleteTask(nonExistentId))
        .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining("not found");
    }

}
