package ru.egorov.auditspringbootstarter;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.math.BigDecimal;
import java.util.UUID;

@Aspect
public class AuditAspect {

    @Before("@annotation(ru.egorov.auditspringbootstarter.Auditable)")
    public void auditMethod(JoinPoint jp) {
        String methodName = jp.getSignature().getName();
        Object[] args = jp.getArgs();
        switch (methodName) {
            case "authorization", "register" -> {
                String login = (String) args[0];
                System.out.println(("Player with login " + login + " try to " + methodName));
            }
            case "getPlayerBalance" -> {
                long id = (long) args[0];
                System.out.println(("Player with id " + id + " try to look his balance"));
            }
            case "getPlayerHistory" -> {
                long id = (long) args[0];
                System.out.println(("Player with id " + id + " try to look his transactions history"));
            }
            case "debit", "credit" -> {
                BigDecimal amount = (BigDecimal) args[0];
                UUID uuid = (UUID) args[1];
                long id = (long) args[2];
                System.out.println(("Player with id " + id + " performs a " + methodName + " transaction with amount " + amount + " and UUID " + uuid));
            }
        }
    }
}
