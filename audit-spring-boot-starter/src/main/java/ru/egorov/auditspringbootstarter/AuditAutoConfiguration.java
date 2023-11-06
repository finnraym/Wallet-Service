package ru.egorov.auditspringbootstarter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuditAutoConfiguration {
    @Bean
    public AuditAspect auditAspect() {
        return new AuditAspect();
    }
}
