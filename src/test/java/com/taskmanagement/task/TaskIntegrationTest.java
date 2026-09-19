package com.taskmanagement.task;

import com.taskmanagement.task.task.Task;
import com.taskmanagement.task.task.TaskPriority;
import com.taskmanagement.task.task.TaskRepository;
import com.taskmanagement.task.task.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class TaskIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    public void setup() {
        taskRepository.deleteAll();
    }

    @Test
    public void getAllTasks() throws Exception {

        Task task1 = new Task();
        task1.setName("Task1");
        task1.setDescription("Task1");
        task1.setStatus(TaskStatus.IN_PROGRESS);
        task1.setPriority(TaskPriority.HIGH);
        task1.setDueDate(LocalDate.now());
        taskRepository.save(task1);

        mockMvc.perform(get("/task").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
              
                .andExpect(jsonPath("$.[0].name").value("Task1"));


    }
    @Test
    public void creatTask() throws Exception {

        String jsonRequest = """
                {
                "id": 1,
                "name": "title1",
                "description": "description1",
                "status": "TODO",
                "priority": "LOW",
                "dueDate": "2019-02-02"
                }
                
                """;
        mockMvc.perform(post("/task")
        .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("title1"));


    }

    @Test
    void createTask_shouldReturnBadRequest() throws Exception {
        String jsonRequest = """
                        {
                
                        "name": " ",
                        "description": "description1",
                        "status": "TODO",
                        "priority": "LOW",
                        "dueDate": "2019-02-02" 
                      }
                """
    ;
        mockMvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());


    }

    @Test
    void getTaskById() throws Exception {

        Task task = new Task();
        task.setName("title1");
        task.setDescription("description1");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.LOW);
        task.setDueDate(LocalDate.of(2026, 10, 30));

        taskRepository.save(task);
        Long id = task.getId();
        mockMvc.perform(get("/task/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("title1"));
    }

    @Test
    void shouldThrowException_whenTaskByIdNotFound() throws Exception {

    Long taskId = 99L;

    mockMvc.perform(get("/task/{id}", taskId))
            .andExpect(status().isNotFound());

    }

    @Test
    void updateTask() throws Exception {
        Task task = new Task();
        task.setName("title1");
        task.setDescription("description1");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.LOW);
        task.setDueDate(LocalDate.of(2026, 10, 30));
         Task savedTask= taskRepository.save(task);

        Long taskId = savedTask.getId();

        String taskRequest = """
                {
                "id": 1,
                "name": "title1",
                "description": "description1",
                "status": "TODO",
                  "priority": "MEDIUM",
                   "dueDate": "2026-02-02" 
                        }
                
        """;

        mockMvc.perform(put("/task/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskRequest))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("title1"));



    }

    @Test
    void updateTask_shouldReturnNotFound() throws Exception {
        Long taskId = 99L;
        String TaskRequest = """
                {
                        
                        "name": "task1",
                        "description": "description1",
                        "status": "TODO",
                        "priority": "LOW",
                        "dueDate": "2019-02-02" 
                      }
                """;
        mockMvc.perform(put("/task/{id}", taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TaskRequest))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteTask() throws Exception {
        Task task = new Task();
        task.setName("title1");
        task.setDescription("description1");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.LOW);
        task.setDueDate(LocalDate.of(2026, 10, 30));

       Task savedTask= taskRepository.save(task);
       Long taskId = savedTask.getId();

       mockMvc.perform(delete("/task/{id}", taskId))
               .andExpect(status().isOk());

       assertThat(taskRepository.findById(taskId)).isEqualTo(savedTask);
    }
}
