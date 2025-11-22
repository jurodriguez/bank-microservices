package com.test.bank.application.usecases.customer;

import com.test.bank.domain.model.Customer;
import com.test.bank.domain.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class ListCustomersUseCaseTest {

    private CustomerRepository repository;
    private ListCustomersUseCase useCase;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(CustomerRepository.class);
        useCase = new ListCustomersUseCase(repository);
    }

    @Test
    void listCustomersSuccessfully() {

        Customer c1 = Customer.builder()
                .id(1L)
                .name("John")
                .build();

        Customer c2 = Customer.builder()
                .id(2L)
                .name("Maria")
                .build();

        when(repository.findAll())
                .thenReturn(Flux.just(c1, c2));

        StepVerifier.create(useCase.execute())
                .expectNext(c1)
                .expectNext(c2)
                .verifyComplete();

        verify(repository, times(1)).findAll();
    }

    @Test
    void listCustomersEmpty() {

        when(repository.findAll())
                .thenReturn(Flux.empty());

        StepVerifier.create(useCase.execute())
                .verifyComplete();

        verify(repository, times(1)).findAll();
    }
}
