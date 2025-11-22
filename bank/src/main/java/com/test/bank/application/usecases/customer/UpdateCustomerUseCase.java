package com.test.bank.application.usecases.customer;

import com.test.bank.domain.exceptions.BusinessException;
import com.test.bank.domain.model.Customer;
import com.test.bank.domain.model.CustomerError;
import com.test.bank.domain.repository.CustomerRepository;
import com.test.bank.infrastructure.events.messaging.CustomerEventPublisher;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UpdateCustomerUseCase {

    private final CustomerRepository repository;
    private final CustomerEventPublisher customerEventPublisher;

    public Mono<Customer> execute(Long id, Customer updates) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(CustomerError.CUSTOMER_NOT_FOUND)))
                .flatMap(existing -> {

                    if (updates.getName() != null) existing.setName(updates.getName());
                    if (updates.getAge() != null) existing.setAge(updates.getAge());
                    if (updates.getIdentification() != null) existing.setIdentification(updates.getIdentification());
                    if (updates.getPassword() != null) existing.setPassword(updates.getPassword());
                    if (updates.getStatus() != null) existing.setStatus(updates.getStatus());
                    if (updates.getGender() != null) existing.setGender(updates.getGender());
                    if (updates.getAddress() != null) existing.setAddress(updates.getAddress());
                    if (updates.getPhone() != null) existing.setPhone(updates.getPhone());

                    return repository.update(existing)
                            .then(repository.findById(id));
                })
                .doOnNext(customerEventPublisher::publishCustomerUpdated);
    }

}

