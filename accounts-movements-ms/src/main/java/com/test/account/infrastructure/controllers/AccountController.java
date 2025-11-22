package com.test.account.infrastructure.controllers;

import com.test.account.application.dto.AccountRequest;
import com.test.account.application.dto.AccountResponse;
import com.test.account.application.mapper.AccountMapper;
import com.test.account.application.usecases.account.CreateAccountUseCase;
import com.test.account.application.usecases.account.GetAccountUseCase;
import com.test.account.application.usecases.account.ListAccountsUseCase;
import com.test.account.application.usecases.account.UpdateAccountUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/cuentas")
@RequiredArgsConstructor
public class AccountController {

    private final CreateAccountUseCase createUseCase;
    private final UpdateAccountUseCase updateUseCase;
    private final GetAccountUseCase getUseCase;
    private final ListAccountsUseCase listUseCase;
    private final AccountMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<AccountResponse> create(@Valid @RequestBody AccountRequest request) {
        return createUseCase.execute(mapper.toEntity(request))
                .map(mapper::toResponse);
    }

    @PutMapping("/{id}")
    public Mono<AccountResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody AccountRequest request) {

        return updateUseCase.execute(id, mapper.toEntity(request))
                .map(mapper::toResponse);
    }

    @GetMapping("/{id}")
    public Mono<AccountResponse> get(@PathVariable Long id) {
        return getUseCase.execute(id)
                .map(mapper::toResponse);
    }

    @GetMapping
    public Flux<AccountResponse> list() {
        return listUseCase.execute()
                .map(mapper::toResponse);
    }
}

