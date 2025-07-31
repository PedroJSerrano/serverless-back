package pjserrano.login.infrastructure.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import pjserrano.login.application.port.out.UserRepositoryPort;
import pjserrano.login.domain.UserPrincipal;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Configuration
@RequiredArgsConstructor
public class JdbcUserRepositoryAdapter {

    private final JdbcTemplate jdbcTemplate;

    // Razón: La lógica se ajusta para usar una consulta que incluya los roles.
    // Se asume que los roles se almacenan como una cadena separada por comas en la BBDD.
    @Bean
    public UserRepositoryPort jdbcUserRepositoryAdapter() {
        return username -> {
            String sql = "SELECT username, password, roles FROM users WHERE username = ?";
            try {
                // Mapeador de fila personalizado para crear UserPrincipal
                RowMapper<UserPrincipal> rowMapper = (rs, rowNum) -> {
                    String rolesString = rs.getString("roles");
                    List<String> roles = Arrays.asList(rolesString.split(","));
                    return new UserPrincipal(rs.getString("username"), rs.getString("password"), roles);
                };

                UserPrincipal principal = jdbcTemplate.queryForObject(sql, rowMapper, username);
                return Optional.of(principal);
            } catch (Exception e) {
                return Optional.empty();
            }
        };
    }
}
