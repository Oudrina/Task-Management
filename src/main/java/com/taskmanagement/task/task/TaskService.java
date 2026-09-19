package com.taskmanagement.task.task;

import com.taskmanagement.task.exception.TaskNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    //
    public TaskResponse createTask(TaskRequest taskRequest) {

        Task createdTask = taskMapper.toEntity(taskRequest);
        Task savedTask = taskRepository.save(createdTask);

        return taskMapper.toResponse(savedTask);

    }

    //
    public List<TaskResponse> getTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(taskMapper::toResponse).toList();
    }


//
    public TaskResponse getTaskById( Long id) {
        Task task = taskRepository.findById(id).orElseThrow(
                ()->   new TaskNotFoundException("Task with id: " + id + " not found")
        );
     return  taskMapper.toResponse(task);
    }

//   Comments needed
    @Transactional
    public TaskResponse updateTask( Long id, TaskRequest taskRequest  ) {

         Task task = taskRepository.findById(id).orElseThrow(
                 ()->   new TaskNotFoundException("Task with id: " + id + " not found")
         );

          task.setName(taskRequest.getName());
          task.setDescription(taskRequest.getDescription());
          task.setStatus(TaskStatus.valueOf(taskRequest.getStatus()));
          taskRepository.save(task);

          return taskMapper.toResponse(task);
    }


    @Transactional
    public void deleteTask( Long id ) {
        Task task = taskRepository.findById(id).orElseThrow(
                ()->   new TaskNotFoundException("Task with id: " + id + " not found")

        );
        taskRepository.delete(task);
    }
}
