package ru.egorov.in.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


/**
 * Player data transfer object
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDTO {

    private String login;
    private BigDecimal balance;
}



