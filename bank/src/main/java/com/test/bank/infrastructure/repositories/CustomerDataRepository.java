package com.test.bank.infrastructure.repositories;

import com.test.bank.infrastructure.repositories.data.CustomerData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CustomerDataRepository extends ReactiveCrudRepository<CustomerData, Long> {
}
