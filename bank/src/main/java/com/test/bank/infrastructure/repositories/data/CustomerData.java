package com.test.bank.infrastructure.repositories.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("customers")
@Data
public class CustomerData {

    @Id
    private Long id;

    private String name;
    private String gender;
    private Integer age;
    private String identification;
    private String address;
    private String phone;

    private String password;
    private Boolean status;
}

