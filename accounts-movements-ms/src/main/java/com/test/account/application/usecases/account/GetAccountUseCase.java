package com.test.account.application.usecases.account;

import com.test.account.domain.exceptions.BusinessException;
import com.test.account.domain.model.Account;
import com.test.account.domain.model.AccountError;
import com.test.account.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GetAccountUseCase {

    private final AccountRepository repository;

    public Mono<Account> execute(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(AccountError.ACCOUNT_NOT_FOUND)));
    }
}
