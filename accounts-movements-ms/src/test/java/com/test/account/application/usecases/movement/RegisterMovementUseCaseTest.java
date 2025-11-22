package com.test.account.application.usecases.movement;

import com.test.account.domain.exceptions.BusinessException;
import com.test.account.domain.model.*;
import com.test.account.domain.repository.AccountRepository;
import com.test.account.domain.repository.MovementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

class RegisterMovementUseCaseTest {

    private AccountRepository accountRepository;
    private MovementRepository movementRepository;
    private RegisterMovementUseCase useCase;

    @BeforeEach
    void setup() {
        accountRepository = Mockito.mock(AccountRepository.class);
        movementRepository = Mockito.mock(MovementRepository.class);
        useCase = new RegisterMovementUseCase(accountRepository, movementRepository);
    }

    private Account sampleAccount() {
        return Account.builder()
                .id(10L)
                .accountNumber("ACC-123")
                .customerId(55L)
                .accountType(AccountType.SAVINGS)
                .balance(BigDecimal.valueOf(500))
                .status(AccountStatus.ACTIVE)
                .build();
    }

    private Movement sampleMovement(MovementType type, BigDecimal amount) {
        return Movement.builder()
                .movementType(type)
                .amount(amount)
                .description("Test movement")
                .build();
    }

    @Test
    void registerDepositSuccess() {

        Account account = sampleAccount();
        Movement movement = sampleMovement(MovementType.DEPOSIT, BigDecimal.valueOf(200));

        Mockito.when(accountRepository.findByAccountNumber("ACC-123"))
                .thenReturn(Mono.just(account));

        Mockito.when(movementRepository.save(Mockito.any(Movement.class)))
                .thenReturn(Mono.just(movement));

        Mockito.when(accountRepository.update(Mockito.any(Account.class)))
                .thenReturn(Mono.just(account));

        StepVerifier.create(useCase.execute("ACC-123", movement))
                .expectNextMatches(m ->
                        m.getBalanceAfter().equals(BigDecimal.valueOf(700)) &&
                                m.getAccountId().equals(10L)
                )
                .verifyComplete();
    }

    @Test
    void registerWithdrawSuccess() {

        Account account = sampleAccount();
        Movement movement = sampleMovement(MovementType.WITHDRAW, BigDecimal.valueOf(300));

        Mockito.when(accountRepository.findByAccountNumber("ACC-123"))
                .thenReturn(Mono.just(account));

        Mockito.when(movementRepository.save(Mockito.any(Movement.class)))
                .thenReturn(Mono.just(movement));

        Mockito.when(accountRepository.update(Mockito.any(Account.class)))
                .thenReturn(Mono.just(account));

        StepVerifier.create(useCase.execute("ACC-123", movement))
                .expectNextMatches(m ->
                        m.getBalanceAfter().equals(BigDecimal.valueOf(200))
                )
                .verifyComplete();
    }

    @Test
    void insufficientBalanceError() {

        Account account = sampleAccount();
        Movement movement = sampleMovement(MovementType.WITHDRAW, BigDecimal.valueOf(800));

        Mockito.when(accountRepository.findByAccountNumber("ACC-123"))
                .thenReturn(Mono.just(account));

        StepVerifier.create(useCase.execute("ACC-123", movement))
                .expectErrorMatches(e ->
                        e instanceof BusinessException &&
                                ((BusinessException) e).getCode()
                                        .equals(AccountError.INSUFFICIENT_BALANCE.getCode())
                )
                .verify();
    }

    @Test
    void accountNotFound() {

        Movement movement = sampleMovement(MovementType.DEPOSIT, BigDecimal.valueOf(200));

        Mockito.when(accountRepository.findByAccountNumber("ACC-123"))
                .thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute("ACC-123", movement))
                .expectErrorMatches(e ->
                        e instanceof BusinessException &&
                                ((BusinessException) e).getCode()
                                        .equals(AccountError.ACCOUNT_NOT_FOUND.getCode())
                )
                .verify();
    }

    @Test
    void movementSaveError() {

        Account account = sampleAccount();
        Movement movement = sampleMovement(MovementType.DEPOSIT, BigDecimal.valueOf(200));

        Mockito.when(accountRepository.findByAccountNumber("ACC-123"))
                .thenReturn(Mono.just(account));

        Mockito.when(movementRepository.save(Mockito.any()))
                .thenReturn(Mono.error(new RuntimeException("DB error")));

        StepVerifier.create(useCase.execute("ACC-123", movement))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void accountUpdateError() {

        Account account = sampleAccount();
        Movement movement = sampleMovement(MovementType.DEPOSIT, BigDecimal.valueOf(200));

        Mockito.when(accountRepository.findByAccountNumber("ACC-123"))
                .thenReturn(Mono.just(account));

        Mockito.when(movementRepository.save(Mockito.any()))
                .thenReturn(Mono.just(movement));

        Mockito.when(accountRepository.update(Mockito.any()))
                .thenReturn(Mono.error(new RuntimeException("DB update error")));

        StepVerifier.create(useCase.execute("ACC-123", movement))
                .expectError(RuntimeException.class)
                .verify();
    }
}
