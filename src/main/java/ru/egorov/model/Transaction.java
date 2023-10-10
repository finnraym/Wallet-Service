package ru.egorov.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class Transaction {
    private Long id;
    private String type;
    private Long playerId;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    private BigDecimal amount;
    private UUID transactionIdentifier;
    public Transaction() {
    }

    public Transaction(String type, Long playerId, BigDecimal balanceBefore, BigDecimal balanceAfter, BigDecimal amount, UUID transactionIdentifier) {
        this.type = type;
        this.playerId = playerId;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
        this.amount = amount;
        this.transactionIdentifier = transactionIdentifier;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public UUID getTransactionIdentifier() {
        return transactionIdentifier;
    }

    public void setTransactionIdentifier(UUID transactionIdentifier) {
        this.transactionIdentifier = transactionIdentifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(type, that.type) && Objects.equals(playerId, that.playerId) && Objects.equals(balanceBefore, that.balanceBefore) && Objects.equals(balanceAfter, that.balanceAfter) && Objects.equals(amount, that.amount) && Objects.equals(transactionIdentifier, that.transactionIdentifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, playerId, balanceBefore, balanceAfter, amount, transactionIdentifier);
    }
}
