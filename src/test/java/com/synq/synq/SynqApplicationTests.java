package com.synq.synq;

import com.synq.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SynqApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private EmailService service;

	@Test
	void sendEmailTest()
	{
		service.sendEmail(
				"abhinavjaipanwar@gmail.com",
				"Testing the email service",
				"this is SYNQ project"
		);
	}
}
