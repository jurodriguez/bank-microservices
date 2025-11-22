package com.test.account.domain.repository;

import com.test.account.domain.model.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountRepository {

    Mono<Account> save(Account account);

    Mono<Account> update(Account account);

    Mono<Account> findByAccountNumber(String accountNumber);

    Mono<Account> findById(Long id);

    Flux<Account> findAll();

    Flux<Account> findByCustomerId(Long customerId);
}
