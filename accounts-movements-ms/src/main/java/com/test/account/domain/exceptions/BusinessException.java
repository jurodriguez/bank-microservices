package com.test.account.domain.exceptions;

import com.test.account.domain.model.AccountError;

public class BusinessException extends RuntimeException {

    private final String code;
    private final AccountError error;

    public BusinessException(AccountError error) {
        super(error.getMessage());
        this.code = error.getCode();
        this.error = error;
    }

    public String getCode() {
        return code;
    }

    public AccountError getError() {
        return error;
    }
}
