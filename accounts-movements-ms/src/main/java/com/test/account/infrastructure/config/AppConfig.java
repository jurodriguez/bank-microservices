package com.test.account.infrastructure.config;

import com.test.account.application.usecases.account.*;
import com.test.account.application.usecases.movement.GetMovementUseCase;
import com.test.account.application.usecases.movement.ListMovementsUseCase;
import com.test.account.application.usecases.movement.RegisterMovementUseCase;
import com.test.account.application.usecases.movement.UpdateMovementUseCase;
import com.test.account.domain.repository.AccountRepository;
import com.test.account.domain.repository.MovementRepository;
import com.test.account.infrastructure.repositories.CustomerCacheRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public CreateAccountUseCase createAccountUseCase(AccountRepository repository) {
        return new CreateAccountUseCase(repository);
    }

    @Bean
    public UpdateAccountUseCase updateAccountUseCase(AccountRepository repository) {
        return new UpdateAccountUseCase(repository);
    }

    @Bean
    public ListAccountsUseCase listAccountsUseCase(AccountRepository repository) {
        return new ListAccountsUseCase(repository);
    }

    @Bean
    public GetAccountUseCase getAccountUseCase(AccountRepository repository) {
        return new GetAccountUseCase(repository);
    }

    @Bean
    public AccountReportUseCase accountReportUseCase(MovementRepository movementRepository, AccountRepository accountRepository,
                                                     CustomerCacheRepository customerCacheRepository) {
        return new AccountReportUseCase(movementRepository, accountRepository, customerCacheRepository);
    }

    @Bean
    public ListMovementsUseCase listMovementsUseCase(MovementRepository repository) {
        return new ListMovementsUseCase(repository);
    }

    @Bean
    public GetMovementUseCase getMovementUseCase(MovementRepository repository) {
        return new GetMovementUseCase(repository);
    }

    @Bean
    public RegisterMovementUseCase registerMovementUseCase(
            AccountRepository accountRepository,
            MovementRepository movementRepository
    ) {
        return new RegisterMovementUseCase(accountRepository, movementRepository);
    }

    @Bean
    public UpdateMovementUseCase updateMovementUseCase(MovementRepository repository) {
        return new UpdateMovementUseCase(repository);
    }
}
