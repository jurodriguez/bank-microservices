package com.test.account.application.usecases.movement;

import com.test.account.domain.model.Movement;
import com.test.account.domain.repository.MovementRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class ListMovementsUseCase {

    private final MovementRepository repository;

    public Flux<Movement> execute() {
        return repository.findAll();
    }
}
