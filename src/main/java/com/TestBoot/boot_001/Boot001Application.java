package com.TestBoot.boot_001;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class Boot001Application {

	public static void main(String[] args) {
		SpringApplication.run(Boot001Application.class, args);
	}

}
