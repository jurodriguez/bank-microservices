package com.test.bank.application.usecases.customer;

import com.test.bank.domain.exceptions.BusinessException;
import com.test.bank.domain.model.CustomerError;
import com.test.bank.domain.repository.CustomerRepository;
import com.test.bank.infrastructure.events.messaging.CustomerEventPublisher;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DeleteCustomerUseCase {

    private final CustomerRepository repository;
    private final CustomerEventPublisher customerEventPublisher;

    public Mono<Void> execute(Long id) {

        return repository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(CustomerError.CUSTOMER_NOT_FOUND)))
                .flatMap(customer -> repository.deleteById(id)
                        .doOnSuccess(v -> customerEventPublisher.publishCustomerDeleted(customer))
                );
    }
}