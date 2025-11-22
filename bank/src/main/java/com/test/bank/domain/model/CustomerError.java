package com.test.bank.domain.model;

public enum CustomerError {

    NAME_REQUIRED("CLI-001", "Name is required"),
    INVALID_PHONE("CLI-002", "Invalid phone number"),
    CUSTOMER_NOT_FOUND("CLI-404", "Customer not found");

    private final String code;
    private final String message;

    CustomerError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
