package ru.egorov.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * The type Transaction.
 */
public class Transaction {
    private Long id;
    private String type;
    private Long playerId;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    private BigDecimal amount;
    private UUID transactionIdentifier;

    /**
     * Instantiates a new Transaction.
     */
    public Transaction() {
    }

    /**
     * Instantiates a new Transaction.
     *
     * @param type                  the type
     * @param playerId              the player id
     * @param balanceBefore         the balance before
     * @param balanceAfter          the balance after
     * @param amount                the amount
     * @param transactionIdentifier the transaction identifier
     */
    public Transaction(String type, Long playerId, BigDecimal balanceBefore, BigDecimal balanceAfter, BigDecimal amount, UUID transactionIdentifier) {
        this.type = type;
        this.playerId = playerId;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
        this.amount = amount;
        this.transactionIdentifier = transactionIdentifier;
    }

    /**
     * Gets balance before.
     *
     * @return the balance before
     */
    public BigDecimal getBalanceBefore() {
        return balanceBefore;
    }

    /**
     * Sets balance before.
     *
     * @param balanceBefore the balance before
     */
    public void setBalanceBefore(BigDecimal balanceBefore) {
        this.balanceBefore = balanceBefore;
    }

    /**
     * Gets balance after.
     *
     * @return the balance after
     */
    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    /**
     * Sets balance after.
     *
     * @param balanceAfter the balance after
     */
    public void setBalanceAfter(BigDecimal balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    /**
     * Gets amount.
     *
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets amount.
     *
     * @param amount the amount
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets player id.
     *
     * @return the player id
     */
    public Long getPlayerId() {
        return playerId;
    }

    /**
     * Sets player id.
     *
     * @param playerId the player id
     */
    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    /**
     * Gets transaction identifier.
     *
     * @return the transaction identifier
     */
    public UUID getTransactionIdentifier() {
        return transactionIdentifier;
    }

    /**
     * Sets transaction identifier.
     *
     * @param transactionIdentifier the transaction identifier
     */
    public void setTransactionIdentifier(UUID transactionIdentifier) {
        this.transactionIdentifier = transactionIdentifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(type, that.type) && Objects.equals(playerId, that.playerId) && Objects.equals(balanceBefore, that.balanceBefore) && Objects.equals(balanceAfter, that.balanceAfter) && Objects.equals(amount, that.amount) && Objects.equals(transactionIdentifier, that.transactionIdentifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, playerId, balanceBefore, balanceAfter, amount, transactionIdentifier);
    }
}
