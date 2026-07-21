package com.lockedIn.lockedIn_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContentRequest {
    @NotBlank
    private String content;
    @NotBlank
    private String source;
}
