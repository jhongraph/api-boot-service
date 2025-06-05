package com.TestBoot.boot_001;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.TimeZone;

@SpringBootApplication
@EnableAsync
public class Boot001Application {

	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("America/Santo_Domingo"));
	}

	public static void main(String[] args) {
		SpringApplication.run(Boot001Application.class, args);
	}

}
