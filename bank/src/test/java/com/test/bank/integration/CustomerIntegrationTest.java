package com.test.bank.integration;

import com.test.bank.application.dto.CustomerRequest;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureWebTestClient
@Testcontainers
public class CustomerIntegrationTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("bank_db")
            .withUsername("root")
            .withPassword("root");

    @Container
    static RabbitMQContainer rabbit = new RabbitMQContainer("rabbitmq:3-management");

    @Autowired
    private WebTestClient client;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {

        registry.add("spring.r2dbc.url", () ->
                "r2dbc:pool:mysql://" + mysql.getHost() + ":" + mysql.getMappedPort(3306) + "/bank_db"
        );

        registry.add("spring.r2dbc.username", mysql::getUsername);
        registry.add("spring.r2dbc.password", mysql::getPassword);

        registry.add("spring.liquibase.enabled", () -> false);

        registry.add("spring.rabbitmq.host", rabbit::getHost);
        registry.add("spring.rabbitmq.port", () -> rabbit.getMappedPort(5672));
        registry.add("spring.rabbitmq.username", rabbit::getAdminUsername);
        registry.add("spring.rabbitmq.password", rabbit::getAdminPassword);
    }


    @Test
    void shouldCreateCustomerAndPublishEvent() {

        CustomerRequest request = new CustomerRequest();
        request.setName("Juan");
        request.setGender("Masculino");
        request.setAge(29);
        request.setIdentification("ABC123987");
        request.setAddress("Calle 3");
        request.setPhone("123456789");
        request.setPassword("password");
        request.setStatus(true);

        client.post()
                .uri("/clientes")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Juan");

        rabbitTemplate.convertAndSend("test-exchange", "test.key", "PING");
    }
}
