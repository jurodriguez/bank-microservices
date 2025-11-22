package com.test.account.domain.model;

import lombok.Data;

@Data
public class CustomerSnapshot {
    private Long id;
    private String name;
    private String identification;
    private Boolean status;
}
