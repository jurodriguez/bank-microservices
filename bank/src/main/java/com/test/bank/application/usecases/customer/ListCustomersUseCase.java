package com.test.bank.application.usecases.customer;

import com.test.bank.domain.model.Customer;
import com.test.bank.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class ListCustomersUseCase {

    private final CustomerRepository repository;

    public Flux<Customer> execute() {
        return repository.findAll();
    }
}
