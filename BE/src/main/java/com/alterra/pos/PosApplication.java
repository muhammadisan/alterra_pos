package com.alterra.pos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class PosApplication {

	public static void main(String[] args) {
		SpringApplication.run(PosApplication.class, args);
	}

}
