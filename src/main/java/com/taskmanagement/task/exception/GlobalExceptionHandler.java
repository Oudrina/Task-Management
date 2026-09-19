package com.taskmanagement.task.exception;

import com.taskmanagement.task.task.TaskRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;

@ControllerAdvice
public class
GlobalExceptionHandler {


    @ExceptionHandler(TaskNotFoundException.class)
public ResponseEntity<String> TaskNotFoundExceptionHandler(TaskNotFoundException ex){

        return  ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

}
