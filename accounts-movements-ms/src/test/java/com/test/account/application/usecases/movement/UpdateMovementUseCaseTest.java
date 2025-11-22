package com.test.account.application.usecases.movement;

import com.test.account.domain.exceptions.BusinessException;
import com.test.account.domain.model.AccountError;
import com.test.account.domain.model.Movement;
import com.test.account.domain.repository.MovementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

class UpdateMovementUseCaseTest {

    private MovementRepository repository;
    private UpdateMovementUseCase useCase;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(MovementRepository.class);
        useCase = new UpdateMovementUseCase(repository);
    }

    private Movement existingMovement() {
        return Movement.builder()
                .id(1L)
                .accountId(10L)
                .amount(BigDecimal.valueOf(100))
                .movementType(null)
                .description("Initial movement")
                .movementDate(LocalDateTime.of(2024, 1, 1, 10, 0))
                .balanceAfter(BigDecimal.valueOf(100))
                .createdAt(LocalDateTime.of(2024, 1, 1, 10, 0))
                .build();
    }

    private Movement updateRequest() {
        return Movement.builder()
                .description("Updated description")
                .movementDate(LocalDateTime.of(2024, 2, 1, 12, 0))
                .build();
    }

    @Test
    void updateSuccess() {

        Movement existing = existingMovement();
        Movement updates = updateRequest();

        Movement updatedFinal = Movement.builder()
                .id(1L)
                .accountId(10L)
                .amount(existing.getAmount())
                .movementType(existing.getMovementType())
                .balanceAfter(existing.getBalanceAfter())
                .description("Updated description")
                .movementDate(LocalDateTime.of(2024, 2, 1, 12, 0))
                .createdAt(existing.getCreatedAt())
                .build();

        Mockito.when(repository.findById(1L))
                .thenReturn(Mono.just(existing));

        Mockito.when(repository.update(Mockito.any(Movement.class)))
                .thenReturn(Mono.just(updatedFinal));

        StepVerifier.create(useCase.execute(1L, updates))
                .expectNextMatches(m ->
                        m.getId().equals(1L) &&
                                m.getDescription().equals("Updated description") &&
                                m.getMovementDate().equals(LocalDateTime.of(2024, 2, 1, 12, 0))
                )
                .verifyComplete();
    }

    @Test
    void updateMovementNotFound() {

        Mockito.when(repository.findById(1L))
                .thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(1L, updateRequest()))
                .expectErrorMatches(e ->
                        e instanceof BusinessException &&
                                ((BusinessException) e).getCode()
                                        .equals(AccountError.MOVEMENT_NOT_FOUND.getCode())
                )
                .verify();
    }

    @Test
    void updateRepositoryError() {

        Movement existing = existingMovement();

        Mockito.when(repository.findById(1L))
                .thenReturn(Mono.just(existing));

        Mockito.when(repository.update(Mockito.any(Movement.class)))
                .thenReturn(Mono.error(new RuntimeException("DB error")));

        StepVerifier.create(useCase.execute(1L, updateRequest()))
                .expectError(RuntimeException.class)
                .verify();
    }
}
