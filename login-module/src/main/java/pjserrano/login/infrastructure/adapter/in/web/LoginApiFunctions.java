package pjserrano.login.infrastructure.adapter.in.web;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pjserrano.login.application.port.in.LoginUserUseCase;
import pjserrano.login.domain.UserCredentials;
import pjserrano.login.infrastructure.adapter.in.web.dto.LoginRequest;
import pjserrano.login.infrastructure.adapter.in.web.dto.LoginResponse;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class LoginApiFunctions {

    private final LoginUserUseCase loginUserUseCase;

    /* Este es el punto de entrada de la Lambda de Spring Cloud Function.
    El tipo de retorno es Function<LoginRequest, LoginResponse>, que es la firma
    esperada por el adaptador de AWS. El nombre 'loginFunction' es lo que se usará
    en la variable de entorno de Lambda (SPRING_CLOUD_FUNCTION_DEFINITION=loginFunction).*/
    @Bean
    public Function<LoginRequest, LoginResponse> loginFunction() {
        return request -> {
            // Mapeo del DTO de entrada a un objeto de dominio
            UserCredentials credentials = new UserCredentials(request.getUsername(), request.getPassword());

            /* Aquí es donde se invoca el caso de uso.
            No se llama a un method, sino a la función 'apply' que Spring Cloud
            Function ha inyectado, que internamente es la lambda
            definida en LoginUserService.*/
            return new LoginResponse(loginUserUseCase.apply(credentials).getJwtToken());
        };
    }
}