package com.test.account.application.usecases.account;

import com.test.account.domain.exceptions.BusinessException;
import com.test.account.domain.model.Account;
import com.test.account.domain.model.AccountError;
import com.test.account.domain.model.AccountStatus;
import com.test.account.domain.model.AccountType;
import com.test.account.domain.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

class UpdateAccountUseCaseTest {

    private AccountRepository repository;
    private UpdateAccountUseCase useCase;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(AccountRepository.class);
        useCase = new UpdateAccountUseCase(repository);
    }

    private Account buildExisting() {
        return Account.builder()
                .id(1L)
                .accountNumber("ACC-123")
                .customerId(10L)
                .accountType(AccountType.SAVINGS)
                .balance(BigDecimal.valueOf(500))
                .status(AccountStatus.ACTIVE)
                .build();
    }

    private Account buildUpdates() {
        return Account.builder()
                .accountNumber("ACC-999")
                .customerId(20L)
                .accountType(AccountType.CHECKING)
                .balance(BigDecimal.valueOf(9999))
                .status(AccountStatus.INACTIVE)
                .build();
    }

    @Test
    void updateSuccess() {

        Account existing = buildExisting();
        Account updates = buildUpdates();

        Account expected = Account.builder()
                .id(1L)
                .accountNumber("ACC-999")
                .customerId(20L)
                .accountType(AccountType.CHECKING)
                .balance(BigDecimal.valueOf(9999))
                .status(AccountStatus.INACTIVE)
                .build();

        Mockito.when(repository.findById(1L)).thenReturn(Mono.just(existing));
        Mockito.when(repository.update(Mockito.any(Account.class)))
                .thenReturn(Mono.just(expected));

        StepVerifier.create(useCase.execute(1L, updates))
                .expectNextMatches(acc ->
                        acc.getId().equals(1L) &&
                                acc.getAccountNumber().equals("ACC-999") &&
                                acc.getCustomerId().equals(20L) &&
                                acc.getAccountType() == AccountType.CHECKING &&
                                acc.getBalance().equals(BigDecimal.valueOf(9999)) &&
                                acc.getStatus() == AccountStatus.INACTIVE
                )
                .verifyComplete();
    }

    @Test
    void updateWhenAccountNotFound() {

        Mockito.when(repository.findById(1L))
                .thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(1L, buildUpdates()))
                .expectErrorMatches(e ->
                        e instanceof BusinessException &&
                                ((BusinessException) e).getCode()
                                        .equals(AccountError.ACCOUNT_NOT_FOUND.getCode())
                )
                .verify();
    }

    @Test
    void updateRepositoryTechnicalError() {

        Account existing = buildExisting();
        Account updates = buildUpdates();

        Mockito.when(repository.findById(1L))
                .thenReturn(Mono.just(existing));

        Mockito.when(repository.update(Mockito.any(Account.class)))
                .thenReturn(Mono.error(new RuntimeException("DB Down")));

        StepVerifier.create(useCase.execute(1L, updates))
                .expectError(RuntimeException.class)
                .verify();
    }
}
