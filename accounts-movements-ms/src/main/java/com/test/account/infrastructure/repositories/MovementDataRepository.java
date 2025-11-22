package com.test.account.infrastructure.repositories;

import com.test.account.infrastructure.repositories.data.MovementData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface MovementDataRepository extends ReactiveCrudRepository<MovementData, Long> {
}
