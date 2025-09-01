package pjserrano.authmanager.infrastructure.adapter.in.web;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pjserrano.authmanager.domain.port.in.LoginUserUseCase;
import pjserrano.authmanager.domain.port.dto.ValidateUserCommand;
import pjserrano.authmanager.infrastructure.adapter.in.web.dto.LoginRequest;
import pjserrano.authmanager.infrastructure.adapter.in.web.dto.LoginResponse;

import java.util.function.Function;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class LoginApiV2Function {

    private final LoginUserUseCase loginUserUseCase;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public Function<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> login() {
        return event -> {
            try {
                // Extraer body del evento HTTP
                LoginRequest request = objectMapper.readValue(event.getBody(), LoginRequest.class);

                // Usar la lógica existente
                ValidateUserCommand credentials = new ValidateUserCommand(request.getUsername(), request.getPassword());
                var response = loginUserUseCase.apply(credentials);

                LoginResponse loginResponse = new LoginResponse(response.getUserId(), response.getJwtToken());

                // Crear respuesta HTTP API v2.0
                return APIGatewayV2HTTPResponse.builder()
                        .withStatusCode(200)
                        .withHeaders(Map.of("Content-Type", "application/json"))
                        .withBody(objectMapper.writeValueAsString(loginResponse))
                        .build();

            } catch (Exception e) {
                return APIGatewayV2HTTPResponse.builder()
                        .withStatusCode(500)
                        .withHeaders(Map.of("Content-Type", "application/json"))
                        .withBody("{\"message\":\"Internal server error\"}")
                        .build();
            }
        };
    }
}
