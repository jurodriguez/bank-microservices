package com.test.bank.application.usecases.customer;

import com.test.bank.domain.model.Customer;
import com.test.bank.domain.repository.CustomerRepository;
import com.test.bank.infrastructure.events.messaging.CustomerEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateCustomerUseCaseTest {

    @Mock
    private CustomerRepository repository;

    @Mock
    private CustomerEventPublisher eventPublisher;

    private CreateCustomerUseCase useCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        useCase = new CreateCustomerUseCase(repository, eventPublisher);
    }

    @Test
    void shouldCreateCustomerSuccessfully() {

        Customer input = Customer.builder()
                .name("John")
                .gender("Male")
                .age(30)
                .identification("123456")
                .address("Main St")
                .phone("3100000000")
                .password("mypassword")
                .status(true)
                .build();

        Customer saved = Customer.builder()
                .id(1L)
                .name("John")
                .gender("Male")
                .age(30)
                .identification("123456")
                .address("Main St")
                .phone("3100000000")
                .password("mypassword")
                .status(true)
                .build();

        when(repository.save(any(Customer.class))).thenReturn(Mono.just(saved));

        StepVerifier.create(useCase.execute(input))
                .expectNext(saved)
                .verifyComplete();

        verify(eventPublisher, times(1)).publishCustomerCreated(saved);
    }
}
