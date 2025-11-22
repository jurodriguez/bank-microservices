package com.test.account.infrastructure.repositories.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("accounts")
@Data
public class AccountData {

    @Id
    private Long id;

    private String accountNumber;
    private Long customerId;
    private String accountType;
    private BigDecimal balance;
    private String status;
}
