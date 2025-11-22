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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class DeleteCustomerUseCaseTest {

    private CustomerRepository repository;
    private CustomerEventPublisher eventPublisher;
    private DeleteCustomerUseCase useCase;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(CustomerRepository.class);
        eventPublisher = Mockito.mock(CustomerEventPublisher.class);
        useCase = new DeleteCustomerUseCase(repository, eventPublisher);
    }

    @Test
    void deleteCustomerSuccessfully() {

        Customer mockCustomer = Customer.builder()
                .id(10L)
                .name("John")
                .gender("Male")
                .age(30)
                .identification("ABC123")
                .address("Street 1")
                .phone("555")
                .password("secret")
                .status(true)
                .build();

        when(repository.findById(10L)).thenReturn(Mono.just(mockCustomer));
        when(repository.deleteById(10L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(10L))
                .expectSubscription()
                .verifyComplete();

        verify(repository).findById(10L);
        verify(repository).deleteById(10L);

        verify(eventPublisher, times(1)).publishCustomerDeleted(mockCustomer);
    }

    @Test
    void deleteCustomerNotFound() {

        when(repository.findById(99L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(99L))
                .expectErrorMatches(error ->
                        error instanceof BusinessException &&
                                ((BusinessException) error).getCode()
                                        .equals(CustomerError.CUSTOMER_NOT_FOUND.getCode())
                )
                .verify();

        verify(repository).findById(99L);
        verify(repository, never()).deleteById(anyLong());

        verify(eventPublisher, never()).publishCustomerDeleted(any());
    }
}
