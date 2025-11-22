package com.test.account.integration;

import com.test.account.application.dto.AccountRequest;
import com.test.account.domain.model.AccountStatus;
import com.test.account.domain.model.AccountType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

@SpringBootTest
@AutoConfigureWebTestClient
@Testcontainers
public class CreateAccountIntegrationTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("bank_db")
            .withUsername("root")
            .withPassword("root");


    @Autowired
    private WebTestClient client;

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {

        registry.add("spring.r2dbc.url", () ->
                "r2dbc:pool:mysql://" + mysql.getHost() + ":" + mysql.getMappedPort(3306) + "/bank_db"
        );

        registry.add("spring.r2dbc.username", mysql::getUsername);
        registry.add("spring.r2dbc.password", mysql::getPassword);

        registry.add("spring.liquibase.enabled", () -> false);
    }

    @Test
    void shouldCreateAccountSuccessfully() {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccountNumber("ACC-1000");
        accountRequest.setAccountType(AccountType.SAVINGS);
        accountRequest.setStatus(AccountStatus.ACTIVE);
        accountRequest.setCustomerId("10");
        accountRequest.setInitialBalance(BigDecimal.valueOf(1000.00));

        client.post()
                .uri("/cuentas")
                .bodyValue(accountRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.accountNumber").isEqualTo("ACC-1000")
                .jsonPath("$.customerId").isEqualTo(10)
                .jsonPath("$.balance").isEqualTo(1000.00);

    }
}
