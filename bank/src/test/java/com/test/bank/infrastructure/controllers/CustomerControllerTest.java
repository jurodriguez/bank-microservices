package com.test.bank.infrastructure.controllers;

import com.test.bank.application.dto.CustomerRequest;
import com.test.bank.application.dto.CustomerResponse;
import com.test.bank.application.dto.CustomerUpdateRequest;
import com.test.bank.application.mapper.CustomerMapper;
import com.test.bank.application.usecases.customer.*;
import com.test.bank.domain.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerControllerTest {

    private CreateCustomerUseCase createCustomerUseCase;
    private UpdateCustomerUseCase updateCustomerUseCase;
    private GetCustomerUseCase getCustomerUseCase;
    private ListCustomersUseCase listCustomersUseCase;
    private DeleteCustomerUseCase deleteCustomerUseCase;
    private CustomerMapper mapper;

    private WebTestClient webTestClient;

    @BeforeEach
    void setup() {

        createCustomerUseCase = mock(CreateCustomerUseCase.class);
        updateCustomerUseCase = mock(UpdateCustomerUseCase.class);
        getCustomerUseCase = mock(GetCustomerUseCase.class);
        listCustomersUseCase = mock(ListCustomersUseCase.class);
        deleteCustomerUseCase = mock(DeleteCustomerUseCase.class);
        mapper = mock(CustomerMapper.class);

        CustomerController controller = new CustomerController(
                createCustomerUseCase,
                updateCustomerUseCase,
                getCustomerUseCase,
                listCustomersUseCase,
                deleteCustomerUseCase,
                mapper
        );

        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    void createCustomerSuccessfully() {

        CustomerRequest request = new CustomerRequest();
        request.setName("John");
        request.setGender("Male");
        request.setAge(30);
        request.setIdentification("ABC123");
        request.setAddress("Street 1");
        request.setPhone("12345");
        request.setPassword("pass123");
        request.setStatus(true);

        Customer domain = new Customer("pass123", true);
        domain.setName("John");

        CustomerResponse response = CustomerResponse.builder()
                .id(1L)
                .name("John")
                .gender("Male")
                .age(30)
                .identification("ABC123")
                .address("Street 1")
                .phone("12345")
                .status(true)
                .build();

        when(mapper.toCustomerDomain(any(CustomerRequest.class))).thenReturn(domain);
        when(createCustomerUseCase.execute(domain)).thenReturn(Mono.just(domain));
        when(mapper.toResponse(domain)).thenReturn(response);

        webTestClient.post()
                .uri("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CustomerResponse.class)
                .isEqualTo(response);

        verify(createCustomerUseCase).execute(domain);
    }

    @Test
    void getCustomerSuccessfully() {

        Customer customer = new Customer("pass", true);
        customer.setId(1L);
        customer.setName("John");

        CustomerResponse response = CustomerResponse.builder()
                .id(1L)
                .name("John")
                .status(true)
                .build();

        when(getCustomerUseCase.execute(1L)).thenReturn(Mono.just(customer));
        when(mapper.toResponse(customer)).thenReturn(response);

        webTestClient.get()
                .uri("/clientes/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerResponse.class)
                .isEqualTo(response);

        verify(getCustomerUseCase).execute(1L);
    }

    @Test
    void listCustomersSuccessfully() {

        Customer c1 = new Customer("pass1", true);
        c1.setId(1L);
        c1.setName("John");

        Customer c2 = new Customer("pass2", false);
        c2.setId(2L);
        c2.setName("Maria");

        CustomerResponse r1 = CustomerResponse.builder().id(1L).name("John").status(true).build();
        CustomerResponse r2 = CustomerResponse.builder().id(2L).name("Maria").status(false).build();

        when(listCustomersUseCase.execute()).thenReturn(Flux.just(c1, c2));
        when(mapper.toResponse(c1)).thenReturn(r1);
        when(mapper.toResponse(c2)).thenReturn(r2);

        webTestClient.get()
                .uri("/clientes")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CustomerResponse.class)
                .contains(r1, r2);

        verify(listCustomersUseCase).execute();
    }

    @Test
    void updateCustomerSuccessfully() {

        CustomerUpdateRequest request = new CustomerUpdateRequest();
        request.setName("Updated");

        Customer domainUpdated = new Customer("pass", true);
        domainUpdated.setName("Updated");

        CustomerResponse response = CustomerResponse.builder()
                .id(1L)
                .name("Updated")
                .status(true)
                .build();

        when(mapper.toCustomerDomain(any(CustomerUpdateRequest.class))).thenReturn(domainUpdated);
        when(updateCustomerUseCase.execute(1L, domainUpdated))
                .thenReturn(Mono.just(domainUpdated));
        when(mapper.toResponse(domainUpdated)).thenReturn(response);

        webTestClient.put()
                .uri("/clientes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerResponse.class)
                .isEqualTo(response);

        verify(updateCustomerUseCase).execute(1L, domainUpdated);
    }

    @Test
    void deleteCustomerSuccessfully() {

        when(deleteCustomerUseCase.execute(1L)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/clientes/1")
                .exchange()
                .expectStatus().isNoContent();

        verify(deleteCustomerUseCase).execute(1L);
    }
}
