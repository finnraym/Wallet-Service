package ru.egorov.dao.impl;

import ru.egorov.config.DBConnectionProvider;
import ru.egorov.dao.TransactionDAO;
import ru.egorov.model.Transaction;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JdbcTransactionDAO implements TransactionDAO {

    private final DBConnectionProvider connectionProvider;

    public JdbcTransactionDAO(DBConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        Optional<Transaction> optionalTransaction = Optional.empty();
        String sql = "SELECT tr.type, tr.player_id, tr.balance_before, tr.balance_after, tr.amount, tr.transaction_identifier " +
                "FROM develop.transaction tr WHERE tr.id=?";
        try(Connection connection = connectionProvider.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                optionalTransaction = Optional.of(getTransactionFromResultSet(resultSet));
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return optionalTransaction;
    }

    @Override
    public List<Transaction> findAll() {
        List<Transaction> all = new ArrayList<>();
        String sql = "SELECT tr.type, tr.player_id, tr.balance_before, tr.balance_after, tr.amount, tr.transaction_identifier " +
                "FROM develop.transaction tr";
        try(Connection connection = connectionProvider.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                all.add(getTransactionFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return all;
    }

    @Override
    public Transaction save(Transaction entity) {
        String sql = "INSERT INTO develop.transaction(type, player_id, balance_before, balance_after, amount, transaction_identifier) VALUES(?, ?, ?, ?, ?, ?)";
        try(Connection connection = connectionProvider.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, entity.getType());
            preparedStatement.setLong(2, entity.getPlayerId());
            preparedStatement.setBigDecimal(3, entity.getBalanceBefore());
            preparedStatement.setBigDecimal(4, entity.getBalanceAfter());
            preparedStatement.setBigDecimal(5, entity.getAmount());
            preparedStatement.setString(6, entity.getTransactionIdentifier().toString());
            preparedStatement.executeUpdate();

            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Transaction> findAllByPlayerId(Long playerId) {
        List<Transaction> all = new ArrayList<>();
        String sql = "SELECT tr.type, tr.player_id, tr.balance_before, tr.balance_after, tr.amount, tr.transaction_identifier " +
                "FROM develop.transaction tr WHERE tr.player_id=?";
        try(Connection connection = connectionProvider.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, playerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                all.add(getTransactionFromResultSet(resultSet));
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return all;
    }

    @Override
    public Optional<Transaction> findByTransactionIdentifier(UUID transactionIdentifier) {
        Optional<Transaction> optionalTransaction = Optional.empty();
        String sql = "SELECT tr.type, tr.player_id, tr.balance_before, tr.balance_after, tr.amount, tr.transaction_identifier " +
                "FROM develop.transaction tr WHERE tr.transaction_identifier=?";
        try(Connection connection = connectionProvider.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, transactionIdentifier.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                optionalTransaction = Optional.of(getTransactionFromResultSet(resultSet));
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return optionalTransaction;
    }

    private Transaction getTransactionFromResultSet(ResultSet resultSet) throws SQLException {
        String type = resultSet.getString("type");
        long playerId = resultSet.getLong("player_id");
        BigDecimal balanceBefore = resultSet.getBigDecimal("balance_before");
        BigDecimal balanceAfter = resultSet.getBigDecimal("balance_after");
        BigDecimal amount = resultSet.getBigDecimal("amount");
        String transactionIdentifier = resultSet.getString("transaction_identifier");

        return new Transaction(type, playerId, balanceBefore, balanceAfter, amount, UUID.fromString(transactionIdentifier));
    }
}
