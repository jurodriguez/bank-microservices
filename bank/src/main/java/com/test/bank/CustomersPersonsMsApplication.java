package com.test.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.test.bank")
public class CustomersPersonsMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomersPersonsMsApplication.class, args);
    }

}
