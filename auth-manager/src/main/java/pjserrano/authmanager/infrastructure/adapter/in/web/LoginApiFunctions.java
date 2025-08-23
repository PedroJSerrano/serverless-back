package pjserrano.authmanager.infrastructure.adapter.in.web;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pjserrano.authmanager.domain.port.in.LoginUserUseCase;
import pjserrano.authmanager.domain.port.dto.ValidateUserCommand;
import pjserrano.authmanager.infrastructure.adapter.in.web.dto.LoginRequest;
import pjserrano.authmanager.infrastructure.adapter.in.web.dto.LoginResponse;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class LoginApiFunctions {

    private final LoginUserUseCase loginUserUseCase;

    @Bean
    public Function<LoginRequest, ResponseEntity<LoginResponse>> loginFunction() {
        return request -> {
            ValidateUserCommand credentials = new ValidateUserCommand(request.getUsername(), request.getPassword());

            return new ResponseEntity<>(new LoginResponse(credentials.getUsername(),
                    loginUserUseCase.apply(credentials).getJwtToken()), HttpStatus.OK);
        };
    }
}