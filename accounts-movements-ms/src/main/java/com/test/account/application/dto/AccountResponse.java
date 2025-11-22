package com.test.account.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class AccountResponse {

    private String accountNumber;
    private String customerId;
    private String accountType;
    private BigDecimal balance;
    private String status;
}
