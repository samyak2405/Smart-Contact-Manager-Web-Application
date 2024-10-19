package com.javahunter.scm;

import com.javahunter.scm.services.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class ScmApplicationTests {

	@Autowired
	private EmailService emailService;

	@Test
	void contextLoads() {
	}

	@Test
	void sendEmailTest(){
		log.info("Email send");
		emailService.sendEmail("moonsamyak54@gmail.com","Registration Link","Click here");
	}
}
