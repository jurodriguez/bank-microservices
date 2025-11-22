package com.test.account.application.usecases.account;


import com.test.account.domain.exceptions.BusinessException;
import com.test.account.domain.model.Account;
import com.test.account.domain.model.AccountError;
import com.test.account.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class CreateAccountUseCase {

    private final AccountRepository repository;

    public Mono<Account> execute(Account account) {

        if (account.getAccountType() == null) {
            return Mono.error(new BusinessException(AccountError.INVALID_ACCOUNT_TYPE));
        }

        if (account.getStatus() == null) {
            return Mono.error(new BusinessException(AccountError.INVALID_ACCOUNT_STATUS));
        }

        if (account.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            return Mono.error(new BusinessException(AccountError.INVALID_INITIAL_BALANCE));
        }

        return repository.save(account);
    }
}

