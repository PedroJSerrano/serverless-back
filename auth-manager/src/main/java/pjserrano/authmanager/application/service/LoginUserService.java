package pjserrano.authmanager.application.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pjserrano.authmanager.application.port.in.LoginUserUseCase;
import pjserrano.authmanager.application.port.out.TokenServicePort;
import pjserrano.authmanager.application.port.out.UserRepositoryPort;
import pjserrano.authmanager.domain.UserSession;
import pjserrano.authmanager.domain.exceptions.InvalidCredentialsException;

@Configuration
public class LoginUserService {

    /* La lógica se ajusta para trabajar con el UserPrincipal.
    El caso de uso es agnóstico a la base de datos; solo sabe que recibe
    un UserPrincipal del UserRepositoryPort.*/
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
