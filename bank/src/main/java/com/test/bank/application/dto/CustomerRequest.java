package com.test.bank.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerRequest {

    @NotBlank
    private String name;
    private String gender;

    @NotNull
    @Min(0)
    private Integer age;

    @NotBlank
    private String identification;
    private String address;
    private String phone;

    @NotBlank
    private String password;
    @NotNull
    private Boolean status;
}


