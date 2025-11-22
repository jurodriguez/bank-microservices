package com.test.account.infrastructure.config;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.test.account.domain.exceptions.BusinessException;
import com.test.account.domain.model.AccountStatus;
import com.test.account.domain.model.AccountType;
import com.test.account.domain.model.ErrorResponse;
import com.test.account.domain.model.MovementType;
import org.springframework.core.codec.DecodingException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(BusinessException.class)
    public Mono<ErrorResponse> handleBusiness(BusinessException ex) {
        return Mono.just(new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getCode(),
                ex.getMessage()
        ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();

        return Mono.just(new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "VAL-001",
                message
        ));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ErrorResponse> handleEnumPath(MethodArgumentTypeMismatchException ex) {

        String field = ex.getName();
        String value = ex.getValue() != null ? ex.getValue().toString() : "null";
        String allowed = "";

        if (ex.getRequiredType() == AccountStatus.class)
            allowed = Arrays.toString(AccountStatus.values());

        if (ex.getRequiredType() == AccountType.class)
            allowed = Arrays.toString(AccountType.values());

        if (ex.getRequiredType() == MovementType.class)
            allowed = Arrays.toString(MovementType.values());

        return Mono.just(new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "VAL-ENUM",
                String.format("Invalid value '%s' for field '%s'. Allowed values: %s",
                        value, field, allowed)
        ));
    }

    @ExceptionHandler(ServerWebInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ErrorResponse> handleJsonEnum(ServerWebInputException ex) {

        Throwable cause = ex.getCause();

        if (cause instanceof DecodingException decoding &&
                decoding.getCause() instanceof InvalidFormatException ife) {

            String field = ife.getPath().get(0).getFieldName();
            String value = ife.getValue() != null ? ife.getValue().toString() : "null";
            String allowed = "";

            if (ife.getTargetType() == AccountStatus.class)
                allowed = Arrays.toString(AccountStatus.values());

            if (ife.getTargetType() == AccountType.class)
                allowed = Arrays.toString(AccountType.values());

            if (ife.getTargetType() == MovementType.class)
                allowed = Arrays.toString(MovementType.values());

            return Mono.just(new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "VAL-ENUM",
                    String.format("Invalid value '%s' for field '%s'. Allowed values: %s",
                            value, field, allowed)
            ));
        }

        return Mono.just(new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "BAD_REQUEST",
                ex.getReason()
        ));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ErrorResponse> handleSqlIntegrity(DataIntegrityViolationException ex) {

        return Mono.just(new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "DB-001",
                "Data integrity violation: " + ex.getMostSpecificCause().getMessage()
        ));
    }

    @ExceptionHandler(io.r2dbc.spi.R2dbcDataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ErrorResponse> handleR2dbcIntegrity(io.r2dbc.spi.R2dbcDataIntegrityViolationException ex) {

        return Mono.just(new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "DB-002",
                ex.getMessage()
        ));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ErrorResponse> handleGeneric(Exception ex) {

        return Mono.just(new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "TECH-001",
                "Internal server error"
        ));
    }
}
