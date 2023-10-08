package ru.egorov.service;

import java.math.BigDecimal;

public interface PlayerService {

    BigDecimal getPlayerBalance(Long id);

    boolean updateBalance(Long id, BigDecimal balance);

}
