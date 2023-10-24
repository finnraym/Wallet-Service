package ru.egorov.in.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


public class TransactionRequest {

    private String playerLogin;
    private BigDecimal amount;

    public TransactionRequest() {
    }

    public TransactionRequest(String playerLogin, BigDecimal amount) {
        this.playerLogin = playerLogin;
        this.amount = amount;
    }

    public String getPlayerLogin() {
        return playerLogin;
    }

    public void setPlayerLogin(String playerLogin) {
        this.playerLogin = playerLogin;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
