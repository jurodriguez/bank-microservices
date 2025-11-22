package com.test.account.application.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MovementUpdateRequest {

    @Size(max = 255, message = "description must have max 255 characters")
    private String description;

    private LocalDateTime movementDate;
}
