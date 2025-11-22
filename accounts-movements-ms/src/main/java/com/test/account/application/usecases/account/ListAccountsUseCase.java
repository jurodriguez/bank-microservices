package com.test.account.application.usecases.account;

import com.test.account.domain.model.Account;
import com.test.account.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class ListAccountsUseCase {

    private final AccountRepository repository;

    public Flux<Account> execute() {
        return repository.findAll();
    }
}