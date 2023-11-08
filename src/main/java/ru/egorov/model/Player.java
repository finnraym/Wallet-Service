package ru.egorov.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * The player entity
 */
@Entity
@Table(name = "player", schema = "develop")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Player {

    /*
    * The unique id player entity
    * */
    @Id
    @SequenceGenerator(name = "player_generator", sequenceName = "player_id_seq", allocationSize = 1, schema = "develop")
    @GeneratedValue(generator = "player_generator", strategy = GenerationType.SEQUENCE)
    private Long id;

    /*
     * The unique login player entity
     * */
    @NotNull
    @Column(unique = true)
    private String login;

    @NotNull
    private String password;
    private BigDecimal balance;

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
