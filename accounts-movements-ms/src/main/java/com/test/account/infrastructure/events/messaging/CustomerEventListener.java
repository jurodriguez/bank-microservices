package com.test.account.infrastructure.events.messaging;

import com.test.account.infrastructure.events.shared.CustomerEvent;
import com.test.account.infrastructure.repositories.CustomerCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.test.account.infrastructure.config.RabbitMQConfig.CUSTOMER_QUEUE_ACCOUNTS;


@Component
@RequiredArgsConstructor
public class CustomerEventListener {

    private final CustomerCacheRepository cacheRepository;

    @RabbitListener(queues = CUSTOMER_QUEUE_ACCOUNTS)
    public void onCustomerEvent(CustomerEvent event) {

        switch (event.getEventType()) {

            case CUSTOMER_CREATED, CUSTOMER_UPDATED -> {
                cacheRepository.saveOrUpdate(event);
            }

            case CUSTOMER_DELETED -> {
                cacheRepository.delete(event.getId());
            }
        }
    }
}
