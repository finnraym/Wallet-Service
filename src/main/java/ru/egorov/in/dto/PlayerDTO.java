package ru.egorov.in.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class PlayerDTO {

    private String login;
    private BigDecimal balance;

    public PlayerDTO() {
    }

    public PlayerDTO(String login, BigDecimal balance) {
        this.login = login;
        this.balance = balance;
    }

}



