package com.test.bank.infrastructure.config;

import com.test.bank.application.usecases.customer.*;
import com.test.bank.domain.repository.CustomerRepository;
import com.test.bank.infrastructure.events.messaging.CustomerEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public CreateCustomerUseCase createCustomerUseCase(CustomerRepository repo, CustomerEventPublisher customerEventPublisher) {
        return new CreateCustomerUseCase(repo, customerEventPublisher);
    }

    @Bean
    public GetCustomerUseCase getCustomerUseCase(CustomerRepository repo) {
        return new GetCustomerUseCase(repo);
    }

    @Bean
    public UpdateCustomerUseCase updateCustomerUseCase(CustomerRepository repo, CustomerEventPublisher customerEventPublisher) {
        return new UpdateCustomerUseCase(repo, customerEventPublisher);
    }

    @Bean
    public DeleteCustomerUseCase deleteCustomerUseCase(CustomerRepository repo, CustomerEventPublisher customerEventPublisher) {
        return new DeleteCustomerUseCase(repo, customerEventPublisher);
    }

    @Bean
    public ListCustomersUseCase listCustomersUseCase(CustomerRepository repo) {
        return new ListCustomersUseCase(repo);
    }
}
