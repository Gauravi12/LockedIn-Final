package com.lockedIn.lockedIn_backend.service;

import com.lockedIn.lockedIn_backend.dto.ReportResponse;
import com.lockedIn.lockedIn_backend.entity.FocusSession;
import com.lockedIn.lockedIn_backend.entity.Task;
import com.lockedIn.lockedIn_backend.entity.User;
import com.lockedIn.lockedIn_backend.repository.BlockedAttemptRepo;
import com.lockedIn.lockedIn_backend.repository.FocusSessionRepo;
import com.lockedIn.lockedIn_backend.repository.TaskRepo;
import com.lockedIn.lockedIn_backend.repository.UserRepo;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {
    private final BlockedAttemptRepo blockedAttemptRepo;
    private final FocusSessionRepo focusSessionRepo;
    private final TaskRepo taskRepo;

    public ReportService(BlockedAttemptRepo blockedAttemptRepo, FocusSessionRepo focusSessionRepo, TaskRepo taskRepo ) {
        this.blockedAttemptRepo = blockedAttemptRepo;
        this.focusSessionRepo = focusSessionRepo;
        this.taskRepo = taskRepo;
    }

    public ReportResponse getReport(User user){

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();

        LocalDateTime endOfDay = LocalDate.now().plusDays(1).atStartOfDay();

        List<FocusSession> sessions = focusSessionRepo.findByUserAndSessionStartBetween(user , startOfDay, endOfDay);

        List<Task> completeTasks = taskRepo.findByUserAndCompletedAtBetween(user , startOfDay , endOfDay);
        List<Task> remainingTasks = taskRepo.findByUserAndCompleted(user , false);
        List<String> completedTaskTitle = new ArrayList<>();
        List<String> remainingTaskTitle = new ArrayList<>();
        for(Task task : completeTasks){
            completedTaskTitle.add(task.getTitle());
        }
        for(Task task : remainingTasks){
            remainingTaskTitle.add(task.getTitle());
        }

        long totalStudyTimeInSec = 0;
        long totalDistractionTimeInSec = 0;
        int totalDistractionCount = 0;
        for(FocusSession focusSession : sessions){
            LocalDateTime endTime;
            if(focusSession.getSessionEnd() == null){
                endTime = LocalDateTime.now();
            }else{
                endTime = focusSession.getSessionEnd();
            }
            totalStudyTimeInSec = totalStudyTimeInSec + (java.time.Duration.between(focusSession.getSessionStart(), endTime).getSeconds() - focusSession.getTotalSleepSecond());
            totalDistractionTimeInSec = totalDistractionTimeInSec + (focusSession.getTotalDistractionSecond());
            totalDistractionCount = totalDistractionCount + focusSession.getDistractionCount();
        }

        long hoursS = totalStudyTimeInSec / 3600;
        long minutesS = (totalStudyTimeInSec % 3600) / 60;
        long secondsS = totalStudyTimeInSec % 60;

        String totalStudyTime = hoursS + " hr "
                + minutesS + " min "
                + secondsS + " sec";

        long hoursD = totalDistractionTimeInSec / 3600;
        long minutesD = (totalDistractionTimeInSec % 3600) / 60;
        long secondsD = totalDistractionTimeInSec % 60;

        String totalDistractionTime = hoursD + " hr "
                + minutesD + " min "
                + secondsD + " sec";


        List<String> blockedAttempts = blockedAttemptRepo.findTopDistractions(user , startOfDay,endOfDay,PageRequest.of(0,3));


        ReportResponse response = new ReportResponse();

        response.setStudyTime(totalStudyTime);
        response.setDistractionTime(totalDistractionTime);
        response.setDistractionCount(totalDistractionCount);

        response.setRemainingTask(remainingTaskTitle);
        response.setCompletedTask(completedTaskTitle);

        response.setTopDistractions(blockedAttempts);

        return response;
    }
}
