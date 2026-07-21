package com.lockedIn.lockedIn_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskResponse {
    private long id;
    private String title;
    private boolean completed;
}
