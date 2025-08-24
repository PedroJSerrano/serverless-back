package pjserrano.authmanager.application.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pjserrano.authmanager.domain.port.dto.ValidateUserResponse;
import pjserrano.authmanager.domain.port.in.LoginUserUseCase;
import pjserrano.authmanager.domain.port.out.TokenServicePort;
import pjserrano.authmanager.domain.port.out.UserRepositoryPort;
import pjserrano.authmanager.domain.exceptions.InvalidCredentialsException;

@Configuration
public class LoginUserService {

    /* La lógica se ajusta para trabajar con el User.
    El caso de uso es agnóstico a la base de datos; solo sabe que recibe
    un User del UserRepositoryPort.*/
    @Bean
    public LoginUserUseCase loginUser(UserRepositoryPort userRepository, TokenServicePort tokenService) {

        return credentials -> userRepository.apply(credentials.getUsername())
                .filter(userPrincipal -> userPrincipal.getPassword().equals(credentials.getPassword()))
                .map(userPrincipal -> {
                    String jwtToken = tokenService.apply(userPrincipal); // Se le pasa el User completo
                    return new ValidateUserResponse(userPrincipal.getUsername(), jwtToken);
                })
                .orElseThrow(InvalidCredentialsException::new);
    }
}
