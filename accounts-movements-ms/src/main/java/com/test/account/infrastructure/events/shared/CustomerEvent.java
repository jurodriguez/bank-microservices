package com.test.account.infrastructure.events.shared;

import lombok.Data;

@Data
public class CustomerEvent {

    public enum EventType {
        CUSTOMER_CREATED,
        CUSTOMER_UPDATED,
        CUSTOMER_DELETED
    }

    private EventType eventType;
    private Long id;
    private String name;
    private String identification;
    private Boolean status;
}
