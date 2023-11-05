package ru.egorov.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.egorov.util.UuidConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "transaction", schema = "develop")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @SequenceGenerator(name = "transaction_generator", sequenceName = "transaction_id_seq", allocationSize = 1, schema = "develop")
    @GeneratedValue(generator = "transaction_generator", strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private String type;
    @Column(name = "player_id", nullable = false)
    private Long playerId;
    @Column(name = "balance_before")
    private BigDecimal balanceBefore;
    @Column(name = "balance_after")
    private BigDecimal balanceAfter;

    private BigDecimal amount;

    @Convert(converter = UuidConverter.class)
    @Column(name = "transaction_identifier", unique = true, nullable = false)
    private UUID transactionIdentifier;

    public Transaction(String type, Long playerId, BigDecimal balanceBefore, BigDecimal balanceAfter, BigDecimal amount, UUID transactionIdentifier) {
        this.type = type;
        this.playerId = playerId;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
        this.amount = amount;
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
