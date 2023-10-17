package ru.egorov.dao.impl;

import ru.egorov.config.DBConnectionProvider;
import ru.egorov.dao.PlayerDAO;
import ru.egorov.model.Player;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The jdbc player dao implementation.
 */
public class JdbcPlayerDAO implements PlayerDAO {

    private final DBConnectionProvider connectionProvider;

    public JdbcPlayerDAO(DBConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @Override
    public Optional<Player> findById(Long id) {
        Optional<Player> optionalPlayer = Optional.empty();
        String sql = "SELECT p.id, p.login, p.password, p.balance " +
                "FROM develop.player p WHERE p.id=?";
        try(Connection connection = connectionProvider.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                optionalPlayer = Optional.of(getPlayerFromResultSet(resultSet));
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return optionalPlayer;
    }

    @Override
    public List<Player> findAll() {
        List<Player> all = new ArrayList<>();
        String sql = "SELECT p.id, p.login, p.password, p.balance FROM develop.player p";
        try(Connection connection = connectionProvider.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                all.add(getPlayerFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return all;
    }

    @Override
    public Player save(Player entity) {
        String sql = "INSERT INTO develop.player(login, password, balance) VALUES(?, ?, ?)";
        try(Connection connection = connectionProvider.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, entity.getLogin());
            preparedStatement.setString(2, entity.getPassword());
            preparedStatement.setBigDecimal(3, entity.getBalance());
            preparedStatement.executeUpdate();
            return findByLogin(entity.getLogin()).orElse(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Optional<Player> findByLogin(String login) {
        Optional<Player> optionalPlayer = Optional.empty();
        String sql = "SELECT p.id, p.login, p.password, p.balance FROM develop.player p WHERE p.login=?";
        try(Connection connection = connectionProvider.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                optionalPlayer = Optional.of(getPlayerFromResultSet(resultSet));
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return optionalPlayer;
    }

    @Override
    public boolean updatePlayerBalance(Long id, BigDecimal balance) {
        String sql = "UPDATE develop.player SET balance=? WHERE id=?";
        try(Connection connection = connectionProvider.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setBigDecimal(1, balance);
            preparedStatement.setLong(2, id);
            preparedStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Player getPlayerFromResultSet(ResultSet resultSet) throws SQLException {
        Player player = new Player();
        long id = resultSet.getLong("id");
        String login = resultSet.getString("login");
        String password = resultSet.getString("password");
        BigDecimal balance = resultSet.getBigDecimal("balance");

        player.setId(id);
        player.setLogin(login);
        player.setPassword(password);
        player.setBalance(balance);
        return player;
    }
}
