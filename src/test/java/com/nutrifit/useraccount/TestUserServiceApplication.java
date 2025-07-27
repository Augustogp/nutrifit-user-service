package com.nutrifit.useraccount;

import org.springframework.boot.SpringApplication;

public class TestUserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(UserAccountServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
