package com.test.account.integration;

import com.test.account.application.dto.AccountRequest;
import com.test.account.domain.model.AccountStatus;
import com.test.account.domain.model.AccountType;
import org.junit.jupiter.api.BeforeAll;
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

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

    private static final String INIT_SQL = """
            CREATE TABLE IF NOT EXISTS accounts (
                id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                account_number VARCHAR(30) NOT NULL UNIQUE,
                customer_id BIGINT NOT NULL,
                account_type VARCHAR(50) NOT NULL,
                balance DECIMAL(15,2) NOT NULL DEFAULT 0,
                status VARCHAR(20) NOT NULL
            );
            """;

    @BeforeAll
    static void initializeDatabase() throws Exception {
        String jdbcUrl = mysql.getJdbcUrl();
        try (Connection conn = DriverManager.getConnection(jdbcUrl, mysql.getUsername(), mysql.getPassword());
             Statement stmt = conn.createStatement()) {

            stmt.execute(INIT_SQL);
            System.out.println("✔ Tablas 'accounts' y 'movements' creadas correctamente.");
        }
    }

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
