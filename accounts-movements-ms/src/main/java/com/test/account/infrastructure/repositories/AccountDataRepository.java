package com.test.account.infrastructure.repositories;

import com.test.account.infrastructure.repositories.data.AccountData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface AccountDataRepository extends ReactiveCrudRepository<AccountData, Long> {
}
