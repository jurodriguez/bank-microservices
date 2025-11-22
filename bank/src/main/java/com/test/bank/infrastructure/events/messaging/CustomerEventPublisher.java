package com.test.bank.infrastructure.events.messaging;

import com.test.bank.infrastructure.events.shared.CustomerEvent;
import com.test.bank.domain.model.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import static com.test.bank.infrastructure.config.RabbitMQConfig.*;

@Component
@RequiredArgsConstructor
public class CustomerEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    private CustomerEvent toEvent(Customer customer, CustomerEvent.EventType type) {
        CustomerEvent event = new CustomerEvent();
        event.setEventType(type);
        event.setId(customer.getId());
        event.setName(customer.getName());
        event.setIdentification(customer.getIdentification());
        event.setStatus(customer.getStatus());
        return event;
    }

    public void publishCustomerCreated(Customer customer) {
        rabbitTemplate.convertAndSend(
                CUSTOMER_EXCHANGE,
                RK_CUSTOMER_CREATED,
                toEvent(customer, CustomerEvent.EventType.CUSTOMER_CREATED)
        );
    }

    public void publishCustomerUpdated(Customer customer) {
        rabbitTemplate.convertAndSend(
                CUSTOMER_EXCHANGE,
                RK_CUSTOMER_UPDATED,
                toEvent(customer, CustomerEvent.EventType.CUSTOMER_UPDATED)
        );
    }

    public void publishCustomerDeleted(Customer customer) {
        rabbitTemplate.convertAndSend(
                CUSTOMER_EXCHANGE,
                RK_CUSTOMER_DELETED,
                toEvent(customer, CustomerEvent.EventType.CUSTOMER_DELETED)
        );
    }
}
