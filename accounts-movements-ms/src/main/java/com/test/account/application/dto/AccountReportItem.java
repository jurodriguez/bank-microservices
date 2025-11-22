package com.test.account.application.dto;

import com.test.account.application.utils.FormatUtils;
import com.test.account.domain.model.Account;
import com.test.account.domain.model.Movement;
import com.test.account.domain.model.MovementType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Data
@Builder
public class AccountReportItem {

    private String date;
    private String customerName;
    private String accountNumber;
    private String accountType;
    private String initialBalance;
    private String status;
    private String movementAmount;
    private String availableBalance;

    public static AccountReportItem from(Movement movement, Account account, String customerName) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        BigDecimal initialBalance;

        if (movement.getMovementType() == MovementType.WITHDRAW) {
            initialBalance = movement.getBalanceAfter().add(movement.getAmount());
        } else {
            initialBalance = movement.getBalanceAfter().subtract(movement.getAmount());
        }

        return AccountReportItem.builder()
                .date(movement.getMovementDate().format(formatter))
                .customerName(customerName)
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType().name())
                .initialBalance(FormatUtils.formatAmount(initialBalance))
                .status(account.getStatus().name())
                .movementAmount(FormatUtils.formatAmount(movement.getAmount()))
                .availableBalance(FormatUtils.formatAmount(movement.getBalanceAfter()))
                .build();
    }

}
