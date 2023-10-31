package ru.egorov.in.dto;

import java.util.List;


public record TransactionHistoryResponse(String playerLogin, List<TransactionResponse> transactions) {
}
