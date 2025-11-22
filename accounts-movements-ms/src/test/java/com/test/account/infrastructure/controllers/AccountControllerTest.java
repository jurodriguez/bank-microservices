package com.test.account.infrastructure.controllers;

import com.test.account.application.dto.AccountRequest;
import com.test.account.application.dto.AccountResponse;
import com.test.account.application.mapper.AccountMapper;
import com.test.account.application.usecases.account.CreateAccountUseCase;
import com.test.account.application.usecases.account.GetAccountUseCase;
import com.test.account.application.usecases.account.ListAccountsUseCase;
import com.test.account.application.usecases.account.UpdateAccountUseCase;
import com.test.account.domain.model.Account;
import com.test.account.domain.model.AccountStatus;
import com.test.account.domain.model.AccountType;
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

@WebFluxTest(controllers = AccountController.class)
class AccountControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockitoBean
    private CreateAccountUseCase createUseCase;

    @MockitoBean
    private UpdateAccountUseCase updateUseCase;

    @MockitoBean
    private GetAccountUseCase getUseCase;

    @MockitoBean
    private ListAccountsUseCase listUseCase;

    @MockitoBean
    private AccountMapper mapper;

    private Account account;
    private AccountRequest request;
    private AccountResponse response;

    @BeforeEach
    void setup() {

        account = Account.builder()
                .id(1L)
                .accountNumber("ACC-123")
                .customerId(10L)
                .accountType(AccountType.SAVINGS)
                .balance(BigDecimal.valueOf(1000))
                .status(AccountStatus.ACTIVE)
                .build();

        request = new AccountRequest();
        request.setAccountNumber("ACC-123");
        request.setCustomerId("10");
        request.setAccountType(AccountType.SAVINGS);
        request.setInitialBalance(BigDecimal.valueOf(1000));
        request.setStatus(AccountStatus.ACTIVE);

        response = AccountResponse.builder()
                .accountNumber("ACC-123")
                .customerId("10")
                .accountType(AccountType.SAVINGS.name())
                .balance(BigDecimal.valueOf(1000))
                .status(AccountStatus.ACTIVE.name())
                .build();
    }

    @Test
    void createAccountSuccess() {

        Mockito.when(mapper.toEntity(request)).thenReturn(account);
        Mockito.when(createUseCase.execute(account)).thenReturn(Mono.just(account));
        Mockito.when(mapper.toResponse(account)).thenReturn(response);

        webClient.post()
                .uri("/cuentas")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AccountResponse.class)
                .isEqualTo(response);
    }

    @Test
    void updateAccountSuccess() {

        Mockito.when(mapper.toEntity(request)).thenReturn(account);
        Mockito.when(updateUseCase.execute(1L, account)).thenReturn(Mono.just(account));
        Mockito.when(mapper.toResponse(account)).thenReturn(response);

        webClient.put()
                .uri("/cuentas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AccountResponse.class)
                .isEqualTo(response);
    }

    @Test
    void getAccountByIdSuccess() {

        Mockito.when(getUseCase.execute(1L)).thenReturn(Mono.just(account));
        Mockito.when(mapper.toResponse(account)).thenReturn(response);

        webClient.get()
                .uri("/cuentas/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(AccountResponse.class)
                .isEqualTo(response);
    }

    @Test
    void listAccountsSuccess() {

        Mockito.when(listUseCase.execute()).thenReturn(Flux.just(account));
        Mockito.when(mapper.toResponse(account)).thenReturn(response);

        webClient.get()
                .uri("/cuentas")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AccountResponse.class)
                .hasSize(1)
                .contains(response);
    }
}
