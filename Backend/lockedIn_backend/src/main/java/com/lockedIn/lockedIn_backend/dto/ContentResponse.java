package com.lockedIn.lockedIn_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.internal.constraintvalidators.bv.time.futureorpresent.AbstractFutureOrPresentJavaTimeValidator;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContentResponse {
    private boolean allowed;
    private String roast;
    private String classification;
}
