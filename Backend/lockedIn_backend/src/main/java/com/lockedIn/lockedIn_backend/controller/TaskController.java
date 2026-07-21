package com.lockedIn.lockedIn_backend.controller;

import com.lockedIn.lockedIn_backend.dto.TaskRequest;
import com.lockedIn.lockedIn_backend.dto.TaskResponse;
import com.lockedIn.lockedIn_backend.entity.Task;
import com.lockedIn.lockedIn_backend.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @PostMapping("/add")
    public String addTask(@RequestBody TaskRequest taskRequest){
        taskService.addTask(taskRequest);
        return "Task added";
    }

    @PutMapping("/complete/{id}")
    public String completeTask(@PathVariable Long id){
        System.out.println("COMPLETE TASK HIT : " + id);
        taskService.completeTask(id);
        return "task completed";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
        return "Task deleted";
    }

    @GetMapping("/all")
    public List<TaskResponse> getAllTask(){
        return taskService.getAllTask();
    }

    @GetMapping("/remaining")
    public List<TaskResponse> getRemainingTask(){
        return taskService.getRemainingTask();
    }
}
