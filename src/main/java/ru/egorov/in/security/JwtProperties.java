package ru.egorov.in.security;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ru.egorov.util.YamlPropertySourceFactory;

/**
 * POJO for jwt properties
 */
@Component
@Data
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
public class JwtProperties {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.access}")
    private Long access;
    @Value("${jwt.refresh}")
    private Long refresh;
}
