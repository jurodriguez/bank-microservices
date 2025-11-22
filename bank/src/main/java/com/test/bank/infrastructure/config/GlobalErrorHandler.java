package com.test.bank.infrastructure.config;

import com.test.bank.domain.exceptions.BusinessException;
import com.test.bank.domain.model.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalErrorHandler {


    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
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
