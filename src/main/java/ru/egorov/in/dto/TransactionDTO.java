package ru.egorov.in.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;


public class TransactionDTO {

    private String type;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    private BigDecimal amount;
    private UUID transactionIdentifier;

    public TransactionDTO(String type, BigDecimal balanceBefore, BigDecimal balanceAfter, BigDecimal amount, UUID transactionIdentifier) {
        this.type = type;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
        this.amount = amount;
        this.transactionIdentifier = transactionIdentifier;
    }

    public TransactionDTO() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getBalanceBefore() {
        return balanceBefore;
    }

    public void setBalanceBefore(BigDecimal balanceBefore) {
        this.balanceBefore = balanceBefore;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(BigDecimal balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public UUID getTransactionIdentifier() {
        return transactionIdentifier;
    }

    public void setTransactionIdentifier(UUID transactionIdentifier) {
        this.transactionIdentifier = transactionIdentifier;
    }
}
