package com.lockedIn.lockedIn_backend.service;

import com.lockedIn.lockedIn_backend.dto.TaskRequest;
import com.lockedIn.lockedIn_backend.dto.TaskResponse;
import com.lockedIn.lockedIn_backend.entity.Task;
import com.lockedIn.lockedIn_backend.entity.User;
import com.lockedIn.lockedIn_backend.repository.TaskRepo;
import com.lockedIn.lockedIn_backend.repository.UserRepo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepo taskRepo;
    private final UserRepo userRepo;
    public TaskService(TaskRepo taskRepo , UserRepo userRepo){
        this.userRepo = userRepo;
        this.taskRepo = taskRepo;
    }

    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void addTask(TaskRequest taskRequest){
        User user = getCurrentUser();
        Task task = new Task();
        task.setUser(user);
        task.setTitle(taskRequest.getTitle());
        task.setCreatedAt(java.time.LocalDateTime.now());
        task.setCompleted(false);
        taskRepo.save(task);
    }

    public void completeTask(Long id){
        User user = getCurrentUser();
        Optional<Task> task = taskRepo.findByIdAndUser(id , user);
        if(task.isPresent()){
            if(!task.get().getCompleted()){
                task.get().setCompleted(true);
                task.get().setCompletedAt(java.time.LocalDateTime.now());
                taskRepo.save(task.get());
            }else{
                throw new RuntimeException("task is already completed");
            }
        }else{
            throw new RuntimeException("Task not found");
        }
    }

    public void deleteTask(Long id){
        User user = getCurrentUser();
        Optional<Task> task = taskRepo.findByIdAndUser(id , user);
        if(task.isPresent()){
            taskRepo.deleteById(id);
        }else{
            throw new RuntimeException("Task not found");
        }
    }

    public List<TaskResponse> getAllTask(){
        User user = getCurrentUser();
        List<Task> tasks = taskRepo.findByUser(user);
        List<TaskResponse> taskResponses = new ArrayList<>();
        for(Task num : tasks){
            taskResponses.add(new TaskResponse(num.getId(), num.getTitle(), num.getCompleted()));
        }
        return taskResponses;
    }

    public List<TaskResponse> getRemainingTask() {
        User user = getCurrentUser();
        List<Task> tasks = taskRepo.findByUserAndCompleted(user , false);
        List<TaskResponse> remainingTask = new ArrayList<>();
        for (Task task : tasks) {
            remainingTask.add(new TaskResponse(task.getId(), task.getTitle(), task.getCompleted()));
        }
        return remainingTask;
    }

}
