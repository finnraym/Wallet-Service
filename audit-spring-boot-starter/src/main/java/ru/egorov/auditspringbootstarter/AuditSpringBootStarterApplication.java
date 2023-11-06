package ru.egorov.auditspringbootstarter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {AuditAutoConfiguration.class})
public class AuditSpringBootStarterApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuditSpringBootStarterApplication.class, args);
	}

}
