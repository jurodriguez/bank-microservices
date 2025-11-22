package com.test.account.infrastructure.controllers;

import com.test.account.application.dto.MovementRequest;
import com.test.account.application.dto.MovementResponse;
import com.test.account.application.dto.MovementUpdateRequest;
import com.test.account.application.mapper.MovementMapper;
import com.test.account.application.usecases.movement.GetMovementUseCase;
import com.test.account.application.usecases.movement.ListMovementsUseCase;
import com.test.account.application.usecases.movement.RegisterMovementUseCase;
import com.test.account.application.usecases.movement.UpdateMovementUseCase;
import com.test.account.domain.model.Movement;
import com.test.account.domain.model.MovementType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@WebFluxTest(controllers = MovementController.class)
class MovementControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockitoBean
    private RegisterMovementUseCase registerMovementUseCase;

    @MockitoBean
    private UpdateMovementUseCase updateMovementUseCase;

    @MockitoBean
    private GetMovementUseCase getMovementUseCase;

    @MockitoBean
    private ListMovementsUseCase listMovementsUseCase;

    @MockitoBean
    private MovementMapper mapper;

    private Movement movement;
    private MovementRequest movementRequest;
    private MovementResponse movementResponse;
    private MovementUpdateRequest updateRequest;

    @BeforeEach
    void setup() {

        movement = Movement.builder()
                .id(1L)
                .accountId(10L)
                .movementType(MovementType.DEPOSIT)
                .amount(BigDecimal.valueOf(200))
                .balanceAfter(BigDecimal.valueOf(1200))
                .description("Test movement")
                .movementDate(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        movementRequest = new MovementRequest();
        movementRequest.setAccountNumber("ACC-123");
        movementRequest.setMovementType(MovementType.DEPOSIT);
        movementRequest.setAmount(BigDecimal.valueOf(200));
        movementRequest.setDescription("Test movement");

        movementResponse = MovementResponse.builder()
                .id(1L)
                .accountId(10L)
                .movementType(MovementType.DEPOSIT.name())
                .amount(BigDecimal.valueOf(200))
                .description("Test movement")
                .movementDate(movement.getMovementDate())
                .createdAt(movement.getCreatedAt())
                .build();

        updateRequest = new MovementUpdateRequest();
        updateRequest.setDescription("Updated desc");
    }

    @Test
    void createMovementSuccess() {

        Mockito.when(mapper.toDomain(movementRequest)).thenReturn(movement);
        Mockito.when(registerMovementUseCase.execute("ACC-123", movement))
                .thenReturn(Mono.just(movement));
        Mockito.when(mapper.toResponse(movement)).thenReturn(movementResponse);

        webClient.post()
                .uri("/movimientos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(movementRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(MovementResponse.class)
                .isEqualTo(movementResponse);
    }

    @Test
    void getMovementByIdSuccess() {

        Mockito.when(getMovementUseCase.execute(1L)).thenReturn(Mono.just(movement));
        Mockito.when(mapper.toResponse(movement)).thenReturn(movementResponse);

        webClient.get()
                .uri("/movimientos/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(MovementResponse.class)
                .isEqualTo(movementResponse);
    }

    @Test
    void listMovementsSuccess() {

        Mockito.when(listMovementsUseCase.execute()).thenReturn(Flux.just(movement));
        Mockito.when(mapper.toResponse(movement)).thenReturn(movementResponse);

        webClient.get()
                .uri("/movimientos")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(MovementResponse.class)
                .hasSize(1)
                .contains(movementResponse);
    }

    @Test
    void updateMovementSuccess() {

        Mockito.when(mapper.toEntity(updateRequest)).thenReturn(movement);
        Mockito.when(updateMovementUseCase.execute(1L, movement)).thenReturn(Mono.just(movement));
        Mockito.when(mapper.toResponse(movement)).thenReturn(movementResponse);

        webClient.put()
                .uri("/movimientos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MovementResponse.class)
                .isEqualTo(movementResponse);
    }

}
