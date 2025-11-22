package com.test.bank.application.usecases.customer;

import com.test.bank.domain.exceptions.BusinessException;
import com.test.bank.domain.model.Customer;
import com.test.bank.domain.model.CustomerError;
import com.test.bank.domain.repository.CustomerRepository;
import com.test.bank.infrastructure.events.messaging.CustomerEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UpdateCustomerUseCaseTest {

    private CustomerRepository repository;
    private CustomerEventPublisher eventPublisher;
    private UpdateCustomerUseCase useCase;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(CustomerRepository.class);
        eventPublisher = Mockito.mock(CustomerEventPublisher.class);
        useCase = new UpdateCustomerUseCase(repository, eventPublisher);
    }

    @Test
    void updateCustomerSuccessfully() {

        Customer existing = Customer.builder()
                .id(1L)
                .name("nombre anterior")
                .gender("Masculino")
                .age(30)
                .identification("ABC123")
                .address("Old Street")
                .phone("555-0000")
                .password("oldpass")
                .status(true)
                .build();

        Customer updates = Customer.builder()
                .name("New Name")
                .address("nueva calle")
                .password("newpass")
                .build();

        Customer updated = Customer.builder()
                .id(1L)
                .name("New Name")
                .gender("Masculino")
                .age(30)
                .identification("ABC123")
                .address("nueva calle")
                .phone("555-0000")
                .password("newpass")
                .status(true)
                .build();

        when(repository.findById(1L))
                .thenReturn(Mono.just(existing))
                .thenReturn(Mono.just(updated));

        when(repository.update(existing))
                .thenReturn(Mono.just(1));

        StepVerifier.create(useCase.execute(1L, updates))
                .expectNext(updated)
                .verifyComplete();

        verify(repository, times(2)).findById(1L);
        verify(repository).update(existing);
        verify(eventPublisher, times(1)).publishCustomerUpdated(updated);
    }

    @Test
    void updateCustomerNotFound() {

        when(repository.findById(99L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(99L, new Customer()))
                .expectErrorMatches(error ->
                        error instanceof BusinessException &&
                                ((BusinessException) error).getCode()
                                        .equals(CustomerError.CUSTOMER_NOT_FOUND.getCode())
                )
                .verify();

        verify(repository).findById(99L);
        verify(repository, never()).update(any());
        verify(eventPublisher, never()).publishCustomerUpdated(any());
    }

    @Test
    void updateCustomerPartialFields() {

        Customer existing = Customer.builder()
                .id(1L)
                .name("nombre anterior")
                .gender("Masculino")
                .age(30)
                .identification("ABC123")
                .address("calle anterior")
                .phone("555-0000")
                .password("oldpass")
                .status(true)
                .build();

        Customer updates = Customer.builder()
                .age(50)
                .build();

        Customer updated = Customer.builder()
                .id(1L)
                .name("nombre anterior")
                .gender("Masculino")
                .age(50)
                .identification("ABC123")
                .address("calle anterior")
                .phone("555-0000")
                .password("oldpass")
                .status(true)
                .build();

        when(repository.findById(1L))
                .thenReturn(Mono.just(existing))
                .thenReturn(Mono.just(updated));

        when(repository.update(existing))
                .thenReturn(Mono.just(1));

        StepVerifier.create(useCase.execute(1L, updates))
                .expectNext(updated)
                .verifyComplete();

        verify(repository, times(2)).findById(1L);
        verify(repository).update(existing);
        verify(eventPublisher, times(1)).publishCustomerUpdated(updated);
    }
}
