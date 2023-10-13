package ru.egorov.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * The type Player.
 */
public class Player {
    private Long id;
    private String login;
    private String password;
    private BigDecimal balance;

    /**
     * Instantiates a new Player.
     */
    public Player() {
    }

    /**
     * Instantiates a new Player.
     *
     * @param login    the login
     * @param password the password
     * @param balance  the balance
     */
    public Player(String login, String password, BigDecimal balance) {
        this.login = login;
        this.password = password;
        this.balance = balance;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets login.
     *
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * Sets login.
     *
     * @param login the login
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets balance.
     *
     * @return the balance
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Sets balance.
     *
     * @param balance the balance
     */
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
