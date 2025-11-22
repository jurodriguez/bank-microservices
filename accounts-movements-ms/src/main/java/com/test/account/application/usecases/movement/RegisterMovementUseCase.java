package com.test.account.application.usecases.movement;

import com.test.account.domain.exceptions.BusinessException;
import com.test.account.domain.model.AccountError;
import com.test.account.domain.model.Movement;
import com.test.account.domain.model.MovementType;
import com.test.account.domain.repository.AccountRepository;
import com.test.account.domain.repository.MovementRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class RegisterMovementUseCase {

    private final AccountRepository accountRepository;
    private final MovementRepository movementRepository;

    public Mono<Movement> execute(String accountNumber, Movement movement) {

        return accountRepository.findByAccountNumber(accountNumber)
                .switchIfEmpty(Mono.error(new BusinessException(AccountError.ACCOUNT_NOT_FOUND)))
                .flatMap(account -> {

                    BigDecimal current = account.getBalance();
                    BigDecimal newBalance;

                    if (movement.getMovementType() == MovementType.WITHDRAW) {

                        if (current.compareTo(movement.getAmount()) < 0)
                            return Mono.error(new BusinessException(AccountError.INSUFFICIENT_BALANCE));

                        newBalance = current.subtract(movement.getAmount());

                    } else {
                        newBalance = current.add(movement.getAmount());
                    }

                    movement.setAccountId(account.getId());
                    movement.setBalanceAfter(newBalance);
                    movement.setMovementDate(LocalDateTime.now());
                    movement.setCreatedAt(LocalDateTime.now());

                    account.setBalance(newBalance);

                    return movementRepository.save(movement)
                            .then(accountRepository.update(account))
                            .thenReturn(movement);
                });
    }
}
