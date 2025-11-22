package com.test.bank.application.usecases.customer;

import com.test.bank.domain.exceptions.BusinessException;
import com.test.bank.domain.model.Customer;
import com.test.bank.domain.model.CustomerError;
import com.test.bank.domain.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GetCustomerUseCaseTest {

    private CustomerRepository repository;
    private GetCustomerUseCase useCase;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(CustomerRepository.class);
        useCase = new GetCustomerUseCase(repository);
    }

    @Test
    void getCustomerByIdSuccessfully() {

        Customer customer = Customer.builder()
                .id(1L)
                .name("Juan")
                .gender("Masculino")
                .age(30)
                .identification("ABC123")
                .address("Calle 123")
                .phone("555-1234")
                .password("pass")
                .status(true)
                .build();

        when(repository.findById(1L)).thenReturn(Mono.just(customer));

        StepVerifier.create(useCase.execute(1L))
                .expectNext(customer)
                .verifyComplete();

        verify(repository).findById(1L);
    }

    @Test
    void getCustomerByIdNotFound() {

        when(repository.findById(99L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(99L))
                .expectErrorMatches(error ->
                        error instanceof BusinessException &&
                                ((BusinessException) error).getCode()
                                        .equals(CustomerError.CUSTOMER_NOT_FOUND.getCode())
                )
                .verify();

        verify(repository).findById(99L);
    }

    @Test
    void findAllCustomersSuccessfully() {

        Customer c1 = Customer.builder()
                .id(1L).name("A").identification("1").password("x").status(true).build();

        Customer c2 = Customer.builder()
                .id(2L).name("B").identification("2").password("y").status(true).build();

        when(repository.findAll()).thenReturn(Flux.fromIterable(List.of(c1, c2)));

        StepVerifier.create(useCase.findAll())
                .expectNext(c1)
                .expectNext(c2)
                .verifyComplete();

        verify(repository).findAll();
    }

    @Test
    void findAllCustomersEmpty() {

        when(repository.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(useCase.findAll())
                .verifyComplete();

        verify(repository).findAll();
    }
}
