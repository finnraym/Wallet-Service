package ru.egorov.in.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


public class TransactionHistoryResponse {

    private String playerLogin;
    private List<TransactionDTO> transactions;

    public TransactionHistoryResponse() {
    }

    public TransactionHistoryResponse(String playerLogin, List<TransactionDTO> transactions) {
        this.playerLogin = playerLogin;
        this.transactions = transactions;
    }

    public String getPlayerLogin() {
        return playerLogin;
    }

    public void setPlayerLogin(String playerLogin) {
        this.playerLogin = playerLogin;
    }

    public List<TransactionDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionDTO> transactions) {
        this.transactions = transactions;
    }
}
