package com.test.account.infrastructure.controllers;

import com.test.account.application.dto.MovementRequest;
import com.test.account.application.dto.MovementResponse;
import com.test.account.application.dto.MovementUpdateRequest;
import com.test.account.application.mapper.MovementMapper;
import com.test.account.application.usecases.movement.GetMovementUseCase;
import com.test.account.application.usecases.movement.ListMovementsUseCase;
import com.test.account.application.usecases.movement.RegisterMovementUseCase;
import com.test.account.application.usecases.movement.UpdateMovementUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/movimientos")
@RequiredArgsConstructor
public class MovementController {

    private final RegisterMovementUseCase registerMovementUseCase;
    private final UpdateMovementUseCase updateMovementUseCase;
    private final GetMovementUseCase getMovementUseCase;
    private final ListMovementsUseCase listMovementsUseCase;
    private final MovementMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MovementResponse> create(@RequestBody MovementRequest request) {
        return registerMovementUseCase.execute(request.getAccountNumber(), mapper.toDomain(request))
                .map(mapper::toResponse);
    }

    @GetMapping("/{id}")
    public Mono<MovementResponse> get(@PathVariable Long id) {
        return getMovementUseCase.execute(id)
                .map(mapper::toResponse);
    }

    @GetMapping
    public Flux<MovementResponse> list() {
        return listMovementsUseCase.execute()
                .map(mapper::toResponse);
    }

    @PutMapping("/{id}")
    public Mono<MovementResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody MovementUpdateRequest request
    ) {
        return updateMovementUseCase.execute(
                        id,
                        mapper.toEntity(request)
                )
                .map(mapper::toResponse);
    }

}


