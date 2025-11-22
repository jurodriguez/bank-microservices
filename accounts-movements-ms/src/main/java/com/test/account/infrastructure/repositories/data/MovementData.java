package com.test.account.infrastructure.repositories.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("movements")
@Data
public class MovementData {

    @Id
    private Long id;

    private Long accountId;
    private String movementType;
    private BigDecimal amount;
    private BigDecimal balanceAfter;
    private String description;
    private LocalDateTime movementDate;
    private LocalDateTime createdAt;
}
