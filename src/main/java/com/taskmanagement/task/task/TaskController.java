package com.taskmanagement.task.task;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
@Slf4j
public class TaskController {
    public final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks(){
        log.info("getAllTasks");
       return new ResponseEntity<>(taskService.getTasks() , HttpStatus.OK);
    }


    @PostMapping
    public  ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest taskRequest){
        log.info("  Task created");
        return   new ResponseEntity<>(taskService.createTask(taskRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(  @PathVariable Long id){
       log.info("Get task by id");
        return  new ResponseEntity<>(taskService.getTaskById(id),  HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(   @PathVariable Long id,@Valid @RequestBody TaskRequest taskRequest){
        log.info("Task updated");
        return new ResponseEntity<>( taskService.updateTask(id, taskRequest), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public  void deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
    }
}
