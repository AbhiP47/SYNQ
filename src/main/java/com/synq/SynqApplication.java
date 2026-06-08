package com.synq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SynqApplication {

	public static void main(String[] args) {
		SpringApplication.run(SynqApplication.class, args);
	}

}
