package pjserrano.login.application.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pjserrano.login.application.port.in.LoginUserUseCase;
import pjserrano.login.application.port.out.TokenServicePort;
import pjserrano.login.application.port.out.UserRepositoryPort;
import pjserrano.login.domain.UserSession;
import pjserrano.login.domain.exceptions.InvalidCredentialsException;

@Configuration
public class LoginUserService {

    // Razón: La lógica se ajusta para trabajar con el UserPrincipal.
    // El caso de uso es agnóstico a la base de datos; solo sabe que recibe
    // un UserPrincipal del UserRepositoryPort.
    @Bean
    public LoginUserUseCase loginUser(UserRepositoryPort userRepository, TokenServicePort tokenService) {

        return credentials -> userRepository.apply(credentials.getUsername())
                .filter(userPrincipal -> userPrincipal.getPassword().equals(credentials.getPassword()))
                .map(userPrincipal -> {
                    String jwtToken = tokenService.apply(userPrincipal); // Se le pasa el UserPrincipal completo
                    return new UserSession(userPrincipal.getUsername(), jwtToken);
                })
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));
    }
}
