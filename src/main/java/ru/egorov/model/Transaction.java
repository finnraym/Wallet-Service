package ru.egorov.model;

import java.util.UUID;

public class Transaction {
    private Long id;
    private String type;
    private Long playerId;
    private UUID transactionIdentifier;
    public Transaction() {
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
}
