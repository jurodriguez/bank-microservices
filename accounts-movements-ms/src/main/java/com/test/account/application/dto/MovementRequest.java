package com.test.account.application.dto;

import com.test.account.domain.model.MovementType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MovementRequest {

    @NotBlank(message = "accountNumber is required")
    private String accountNumber;

    @NotNull(message = "movementType is required")
    private MovementType movementType;

    @NotNull(message = "amount is required")
    @Positive(message = "amount must be greater than 0")
    private BigDecimal amount;

    @Size(max = 255, message = "description must have max 255 characters")
    private String description;
}
