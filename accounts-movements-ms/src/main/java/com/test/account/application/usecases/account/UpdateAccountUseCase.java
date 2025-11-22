package com.test.account.application.usecases.account;

import com.test.account.domain.exceptions.BusinessException;
import com.test.account.domain.model.Account;
import com.test.account.domain.model.AccountError;
import com.test.account.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UpdateAccountUseCase {

    private final AccountRepository repository;

    public Mono<Account> execute(Long id, Account newData) {

        return repository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(AccountError.ACCOUNT_NOT_FOUND)))
                .flatMap(existing -> {

                    existing.setAccountNumber(newData.getAccountNumber());
                    existing.setCustomerId(newData.getCustomerId());
                    existing.setAccountType(newData.getAccountType());
                    existing.setBalance(newData.getBalance());
                    existing.setStatus(newData.getStatus());

                    return repository.update(existing);
                });
    }
}
