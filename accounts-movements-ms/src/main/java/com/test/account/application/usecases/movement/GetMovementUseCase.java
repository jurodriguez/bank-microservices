package com.test.account.application.usecases.movement;

import com.test.account.domain.exceptions.BusinessException;
import com.test.account.domain.model.AccountError;
import com.test.account.domain.model.Movement;
import com.test.account.domain.repository.MovementRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GetMovementUseCase {

    private final MovementRepository repository;

    public Mono<Movement> execute(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(AccountError.MOVEMENT_NOT_FOUND)));
    }
}
