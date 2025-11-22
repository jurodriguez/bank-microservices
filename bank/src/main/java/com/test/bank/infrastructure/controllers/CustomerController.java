package com.test.bank.infrastructure.controllers;

import com.test.bank.application.dto.CustomerRequest;
import com.test.bank.application.dto.CustomerResponse;
import com.test.bank.application.dto.CustomerUpdateRequest;
import com.test.bank.application.mapper.CustomerMapper;
import com.test.bank.application.usecases.customer.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class CustomerController {

    private final CreateCustomerUseCase createCustomerUseCase;
    private final UpdateCustomerUseCase updateCustomerUseCase;
    private final GetCustomerUseCase getCustomerUseCase;
    private final ListCustomersUseCase listCustomersUseCase;
    private final DeleteCustomerUseCase deleteCustomerUseCase;
    private final CustomerMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CustomerResponse> create(@Valid @RequestBody CustomerRequest request) {
        return createCustomerUseCase.execute(mapper.toCustomerDomain(request))
                .map(mapper::toResponse);
    }

    @GetMapping("/{id}")
    public Mono<CustomerResponse> get(@PathVariable Long id) {
        return getCustomerUseCase.execute(id)
                .map(mapper::toResponse);
    }

    @GetMapping
    public Flux<CustomerResponse> list() {
        return listCustomersUseCase.execute()
                .map(mapper::toResponse);
    }

    @PutMapping("/{id}")
    public Mono<CustomerResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CustomerUpdateRequest request) {

        return updateCustomerUseCase.execute(id, mapper.toCustomerDomain(request))
                .map(mapper::toResponse);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Long id) {
        return deleteCustomerUseCase.execute(id);
    }
}
