package ru.egorov.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Audit aspect
 */
@Component
@Aspect
public class AuditAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * audit logic
     *
     * @param jp join point
     */
    @Before("@annotation(ru.egorov.aop.Audit)")
    public void logMethod(JoinPoint jp) {
        String methodName = jp.getSignature().getName();
        Object[] args = jp.getArgs();

        switch (methodName) {
            case "authorization", "register" -> {
                String login = (String) args[0];
                log.info("Player with login " + login + " try to " + methodName);
                System.out.println(("Player with login " + login + " try to " + methodName));
            }
            case "getPlayerBalance" -> {
                long id = (long) args[0];
                log.info("Player with id " + id + " try to look his balance");
                System.out.println(("Player with id " + id + " try to look his balance"));
            }
            case "getPlayerHistory" -> {
                long id = (long) args[0];
                log.info("Player with id " + id + " try to look his transactions history");
                System.out.println(("Player with id " + id + " try to look his transactions history"));
            }
            case "debit", "credit" -> {
                BigDecimal amount = (BigDecimal) args[0];
                UUID uuid = (UUID) args[1];
                long id = (long) args[2];
                log.info("Player with id " + id + " performs a " + methodName + " transaction with amount " + amount + " and UUID " + uuid);
                System.out.println(("Player with id " + id + " performs a " + methodName + " transaction with amount " + amount + " and UUID " + uuid));
            }
        }
    }
}
