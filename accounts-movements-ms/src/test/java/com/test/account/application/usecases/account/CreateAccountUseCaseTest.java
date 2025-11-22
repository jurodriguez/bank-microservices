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

class CreateAccountUseCaseTest {

    private AccountRepository repository;
    private CreateAccountUseCase useCase;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(AccountRepository.class);
        useCase = new CreateAccountUseCase(repository);
    }

    private Account buildValidAccount() {
        return Account.builder()
                .id(1L)
                .accountNumber("ACC-12345")
                .customerId(99L)
                .accountType(AccountType.SAVINGS)
                .status(AccountStatus.ACTIVE)
                .balance(BigDecimal.valueOf(100))
                .build();
    }

    @Test
    void createAccountSuccess() {
        Account account = buildValidAccount();

        Mockito.when(repository.save(account)).thenReturn(Mono.just(account));

        StepVerifier.create(useCase.execute(account))
                .expectNext(account)
                .verifyComplete();
    }

    @Test
    void invalidAccountType() {
        Account account = buildValidAccount();
        account.setAccountType(null);

        StepVerifier.create(useCase.execute(account))
                .expectErrorMatches(e ->
                        e instanceof BusinessException &&
                                ((BusinessException) e).getCode().equals(AccountError.INVALID_ACCOUNT_TYPE.getCode())
                )
                .verify();
    }

    @Test
    void invalidStatus() {
        Account account = buildValidAccount();
        account.setStatus(null);

        StepVerifier.create(useCase.execute(account))
                .expectErrorMatches(e ->
                        e instanceof BusinessException &&
                                ((BusinessException) e).getCode().equals(AccountError.INVALID_ACCOUNT_STATUS.getCode())
                )
                .verify();
    }

    @Test
    void negativeInitialBalance() {
        Account account = buildValidAccount();
        account.setBalance(BigDecimal.valueOf(-10));

        StepVerifier.create(useCase.execute(account))
                .expectErrorMatches(e ->
                        e instanceof BusinessException &&
                                ((BusinessException) e).getCode().equals(AccountError.INVALID_INITIAL_BALANCE.getCode())
                )
                .verify();
    }

    @Test
    void repositoryThrowsTechnicalError() {
        Account account = buildValidAccount();

        Mockito.when(repository.save(account))
                .thenReturn(Mono.error(new RuntimeException("DB Down")));

        StepVerifier.create(useCase.execute(account))
                .expectError(RuntimeException.class)
                .verify();
    }
}
