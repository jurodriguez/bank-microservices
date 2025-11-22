package com.test.bank.application.usecases.customer;

import com.test.bank.domain.model.Customer;
import com.test.bank.domain.repository.CustomerRepository;
import com.test.bank.infrastructure.events.messaging.CustomerEventPublisher;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateCustomerUseCase {

    private final CustomerRepository repository;
    private final CustomerEventPublisher eventPublisher;

    public Mono<Customer> execute(Customer customer) {
        return repository.save(customer)
                .doOnNext(eventPublisher::publishCustomerCreated);
    }
}

