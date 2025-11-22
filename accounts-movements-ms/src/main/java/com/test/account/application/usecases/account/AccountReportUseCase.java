package com.test.account.application.usecases.account;

import com.test.account.application.dto.AccountReportItem;
import com.test.account.domain.repository.AccountRepository;
import com.test.account.domain.repository.MovementRepository;
import com.test.account.infrastructure.repositories.CustomerCacheRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@RequiredArgsConstructor
public class AccountReportUseCase {

    private final MovementRepository movementRepository;
    private final AccountRepository accountRepository;
    private final CustomerCacheRepository customerCacheRepository;

    public Flux<AccountReportItem> execute(LocalDate start, LocalDate end, Long customerId) {

        return accountRepository.findByCustomerId(customerId)
                .flatMap(account -> {
                    return movementRepository.findByAccountIdAndDateRange(
                                    account.getId(),
                                    start.atStartOfDay(),
                                    end.atTime(23, 59, 59)
                            )
                            .flatMap(movement ->
                                    customerCacheRepository.findById(customerId)
                                            .map(customer ->
                                                    AccountReportItem.from(
                                                            movement,
                                                            account,
                                                            customer.getName()
                                                    )
                                            )
                                            .defaultIfEmpty(
                                                    AccountReportItem.from(
                                                            movement,
                                                            account,
                                                            ""
                                                    )
                                            )
                            );
                });
    }
}

