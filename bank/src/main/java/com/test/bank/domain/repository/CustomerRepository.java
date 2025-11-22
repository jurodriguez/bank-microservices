package com.test.bank.domain.repository;

import com.test.bank.domain.model.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerRepository {

    Mono<Customer> save(Customer customer);

    Mono<Customer> findById(Long id);

    Flux<Customer> findAll();

    Mono<Integer> update(Customer customer);

    Mono<Void> deleteById(Long id);
}
