package com.test.account.application.dto;

import com.test.account.domain.model.AccountStatus;
import com.test.account.domain.model.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountRequest {

    @NotBlank(message = "customerId is required")
    private String customerId;

    @NotBlank(message = "accountNumber is required")
    @Size(min = 6, max = 30, message = "accountNumber must be between 6 and 30 characters")
    private String accountNumber;

    @NotNull(message = "accountType is required")
    private AccountType accountType;

    @NotNull(message = "initialBalance is required")
    @PositiveOrZero(message = "initialBalance must be greater or equal to 0")
    private BigDecimal initialBalance;

    @NotNull(message = "status is required")
    private AccountStatus status;
}

