package pjserrano.login.infrastructure.adapter.out.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import pjserrano.login.application.port.out.UserRepositoryPort;
import pjserrano.login.domain.UserPrincipal;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
class JdbcUserRepositoryAdapterTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRepositoryPort userRepositoryPort;

    @Configuration
    @Import(JdbcUserRepositoryAdapter.class)
    static class TestConfig { }

    // Razón: Antes de cada test, creamos la tabla y añadimos datos de prueba.
    // Esto asegura que cada test se ejecute en un estado conocido y limpio.
    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS users");
        jdbcTemplate.execute("CREATE TABLE users (username VARCHAR(255) PRIMARY KEY, password VARCHAR(255), roles VARCHAR(255))");
        jdbcTemplate.update("INSERT INTO users (username, password, roles) VALUES (?, ?, ?)",
                "testuser", "password123", "ROLE_USER,ROLE_ADMIN");
    }

    @Test
    void whenUserExists_thenShouldReturnUserPrincipal() {
        // GIVEN - Los datos de prueba ya están en la base de datos por el @BeforeEach

        // WHEN - Ejecutamos la lógica del adaptador
        Optional<UserPrincipal> result = userRepositoryPort.apply("testuser");

        // THEN - Verificamos que el resultado es correcto
        assertTrue(result.isPresent());
        UserPrincipal principal = result.get();
        assertEquals("testuser", principal.getUsername());
        assertEquals("password123", principal.getPassword());
        assertTrue(principal.getRoles().contains("ROLE_USER"));
        assertTrue(principal.getRoles().contains("ROLE_ADMIN"));
    }

    @Test
    void whenUserDoesNotExist_thenShouldReturnEmptyOptional() {
        // GIVEN - El usuario "nonexistent" no está en la base de datos

        // WHEN
        Optional<UserPrincipal> result = userRepositoryPort.apply("nonexistent");

        // THEN
        assertFalse(result.isPresent());
    }
}