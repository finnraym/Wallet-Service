package ru.egorov.in.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * The transaction request
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {

    @NotNull
    private String playerLogin;

    @NotNull
    @DecimalMin(message = "Amount must not less than 0.0!", value = "0.0", inclusive = false)
    private BigDecimal amount;
}
