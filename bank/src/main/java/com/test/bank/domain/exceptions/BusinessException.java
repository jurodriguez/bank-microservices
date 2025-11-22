package com.test.bank.domain.exceptions;

import com.test.bank.domain.model.CustomerError;

public class BusinessException extends RuntimeException {

    private final String code;
    private final CustomerError error;

    public BusinessException(CustomerError error) {
        super(error.getMessage());
        this.code = error.getCode();
        this.error = error;
    }

    public String getCode() { return code; }

    public CustomerError getError() { return error; }
}
