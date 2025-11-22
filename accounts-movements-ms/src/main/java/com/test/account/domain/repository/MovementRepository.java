package com.test.account.domain.repository;

import com.test.account.domain.model.Movement;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface MovementRepository {

    Mono<Movement> save(Movement movement);

    Mono<Movement> findById(Long id);

    Flux<Movement> findAll();

    Mono<Movement> update(Movement movement);

    Flux<Movement> findByAccountIdAndDateRange(Long accountId, LocalDateTime start, LocalDateTime end);

}