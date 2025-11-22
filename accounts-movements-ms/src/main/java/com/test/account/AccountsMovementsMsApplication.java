package com.test.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.test.account")
public class AccountsMovementsMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountsMovementsMsApplication.class, args);
	}

}
