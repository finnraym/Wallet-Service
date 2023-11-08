package ru.egorov.auditspringbootstarter;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Aspect for audit.
 */
@Aspect
public class AuditAspect {

    @Before("@annotation(ru.egorov.auditspringbootstarter.Auditable)")
    public void auditMethod(JoinPoint jp) {
        String methodName = jp.getSignature().getName();
        System.out.println("Method name " + methodName);
        System.out.println("Signature " + jp.getSignature());
        Object[] args = jp.getArgs();
        for (int i = 0; i < args.length; i++) {
            System.out.println("Type: " + args[i].getClass().toString() + "; Value: " + args[i].toString());
        }
    }
}
