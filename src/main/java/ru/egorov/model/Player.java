package ru.egorov.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * The Player entity.
 */
@Entity
@Table(name = "player", schema = "develop")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Player {

    @Id
    @SequenceGenerator(name = "player_generator", sequenceName = "player_id_seq", allocationSize = 1, schema = "develop")
    @GeneratedValue(generator = "player_generator", strategy = GenerationType.SEQUENCE)
    private Long id;

    private String login;

    private String password;
    private BigDecimal balance;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(login, player.login) && Objects.equals(password, player.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password);
    }
}
