package com.test.account.application.usecases.movement;

import com.test.account.domain.exceptions.BusinessException;
import com.test.account.domain.model.AccountError;
import com.test.account.domain.model.Movement;
import com.test.account.domain.repository.MovementRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UpdateMovementUseCase {

    private final MovementRepository movementRepository;

    public Mono<Movement> execute(Long id, Movement updates) {

        return movementRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(AccountError.MOVEMENT_NOT_FOUND)))
                .flatMap(existing -> {

                    if (updates.getDescription() != null) {
                        existing.setDescription(updates.getDescription());
                    }

                    if (updates.getMovementDate() != null) {
                        existing.setMovementDate(updates.getMovementDate());
                    }

                    return movementRepository.update(existing);
                });
    }
}
