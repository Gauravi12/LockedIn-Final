package com.lockedIn.lockedIn_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReportResponse {
    private String studyTime;
    private String distractionTime;
    private int distractionCount;
    private List<String> remainingTask;
    private List<String> completedTask;
    private List<String> topDistractions;
}
