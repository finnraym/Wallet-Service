package ru.egorov.in.dto;

import java.util.List;

/**
 * Response for transaction history
 *
 * @param playerLogin the player login
 * @param transactions the list of transactions
 */
public record TransactionHistoryResponse(String playerLogin, List<TransactionResponse> transactions) {
}
