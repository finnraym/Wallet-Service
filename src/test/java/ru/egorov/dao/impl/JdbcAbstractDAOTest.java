package ru.egorov.dao.impl;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class JdbcAbstractDAOTest {

    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:15.1-alpine");

    @BeforeAll
    static void init() {
        container.start();
    }

    @AfterAll
    static void tearDown() {
        container.stop();
    }
}
