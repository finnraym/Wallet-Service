package ru.egorov.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Player {
    private Long id;
    private String login;
    private String password;
    private BigDecimal balance;

    public Player() {
    }

    public Player(String login, String password, BigDecimal balance) {
        this.login = login;
        this.password = password;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(login, player.login) && Objects.equals(password, player.password) && Objects.equals(balance, player.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password, balance);
    }
}
