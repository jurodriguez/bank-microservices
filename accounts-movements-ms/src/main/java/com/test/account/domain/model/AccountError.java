package com.test.account.domain.model;

import lombok.Getter;

@Getter
public enum AccountError {

    ACCOUNT_NOT_FOUND("ACC-404", "Account not found"),
    INVALID_INITIAL_BALANCE("ACC-001", "Initial balance must be >= 0"),
    INVALID_ACCOUNT_TYPE("ACC-002", "Invalid account type"),
    INVALID_ACCOUNT_STATUS("ACC-003", "Invalid account status"),
    MOVEMENT_NOT_FOUND("MOV-404", "Movement not found"),
    INVALID_AMOUNT("MOV-001", "Invalid Amount"),
    INSUFFICIENT_BALANCE("MOV-002", "Insufficient balance"),
    INVALID_MOVEMENT_VALUE("MOV-003", "Movement value must be > 0"),
    INVALID_MOVEMENT_TYPE("MOV-004", "Invalid movement type");

    private final String code;
    private final String message;

    AccountError(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
