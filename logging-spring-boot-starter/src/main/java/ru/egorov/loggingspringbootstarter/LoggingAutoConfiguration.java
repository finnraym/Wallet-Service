package ru.egorov.loggingspringbootstarter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Starter configuration class logging.
 */
@Configuration
public class LoggingAutoConfiguration {

    /**
     * @return LoggingAspect bean
     */
    @Bean
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
}
