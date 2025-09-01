package pjserrano.authmanager.infrastructure.in.web;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pjserrano.authmanager.domain.port.dto.ValidateUserCommand;
import pjserrano.authmanager.domain.port.dto.ValidateUserResponse;
import pjserrano.authmanager.domain.port.in.LoginUserUseCase;
import pjserrano.authmanager.infrastructure.adapter.in.web.LoginApiFunction;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoginApiFunction Unit Tests")
class LoginApiFunctionUnitTest {

    @Mock
    private LoginUserUseCase loginUserUseCase;

    private LoginApiFunction loginApiFunction;
    private Function<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> loginFunction;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        loginApiFunction = new LoginApiFunction(loginUserUseCase);
        loginFunction = loginApiFunction.login();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Should return 200 and JWT token when login is successful")
    void shouldReturnSuccessResponseWhenLoginIsSuccessful() throws Exception {
        // Given
        String requestBody = "{\"username\":\"testuser\",\"password\":\"testpass\"}";
        APIGatewayV2HTTPEvent event = createHttpEvent(requestBody);
        
        ValidateUserResponse mockResponse = new ValidateUserResponse("testuser", "mock-jwt-token");
        when(loginUserUseCase.apply(any(ValidateUserCommand.class))).thenReturn(mockResponse);

        // When
        APIGatewayV2HTTPResponse response = loginFunction.apply(event);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getHeaders()).containsEntry("Content-Type", "application/json");
        
        String responseBody = response.getBody();
        assertThat(responseBody).contains("testuser");
        assertThat(responseBody).contains("mock-jwt-token");
        
        verify(loginUserUseCase).apply(any(ValidateUserCommand.class));
    }

    @Test
    @DisplayName("Should return 500 when UseCase throws exception")
    void shouldReturnErrorResponseWhenUseCaseThrowsException() {
        // Given
        String requestBody = "{\"username\":\"testuser\",\"password\":\"wrongpass\"}";
        APIGatewayV2HTTPEvent event = createHttpEvent(requestBody);
        
        when(loginUserUseCase.apply(any(ValidateUserCommand.class)))
            .thenThrow(new RuntimeException("Invalid credentials"));

        // When
        APIGatewayV2HTTPResponse response = loginFunction.apply(event);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(500);
        assertThat(response.getHeaders()).containsEntry("Content-Type", "application/json");
        assertThat(response.getBody()).contains("Internal server error");
        
        verify(loginUserUseCase).apply(any(ValidateUserCommand.class));
    }

    @Test
    @DisplayName("Should return 500 when request body is malformed JSON")
    void shouldReturnErrorResponseWhenRequestBodyIsMalformed() {
        // Given
        String malformedJson = "{\"username\":\"testuser\",\"password\":}";
        APIGatewayV2HTTPEvent event = createHttpEvent(malformedJson);

        // When
        APIGatewayV2HTTPResponse response = loginFunction.apply(event);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(500);
        assertThat(response.getHeaders()).containsEntry("Content-Type", "application/json");
        assertThat(response.getBody()).contains("Internal server error");
        
        verify(loginUserUseCase, never()).apply(any(ValidateUserCommand.class));
    }

    @Test
    @DisplayName("Should return 500 when request body is null")
    void shouldReturnErrorResponseWhenRequestBodyIsNull() {
        // Given
        APIGatewayV2HTTPEvent event = createHttpEvent(null);

        // When
        APIGatewayV2HTTPResponse response = loginFunction.apply(event);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(500);
        assertThat(response.getHeaders()).containsEntry("Content-Type", "application/json");
        assertThat(response.getBody()).contains("Internal server error");
        
        verify(loginUserUseCase, never()).apply(any(ValidateUserCommand.class));
    }

    @Test
    @DisplayName("Should return 500 when request body is empty")
    void shouldReturnErrorResponseWhenRequestBodyIsEmpty() {
        // Given
        APIGatewayV2HTTPEvent event = createHttpEvent("");

        // When
        APIGatewayV2HTTPResponse response = loginFunction.apply(event);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(500);
        assertThat(response.getHeaders()).containsEntry("Content-Type", "application/json");
        assertThat(response.getBody()).contains("Internal server error");
        
        verify(loginUserUseCase, never()).apply(any(ValidateUserCommand.class));
    }

    @Test
    @DisplayName("Should pass correct credentials to UseCase")
    void shouldPassCorrectCredentialsToUseCase() throws Exception {
        // Given
        String requestBody = "{\"username\":\"myuser\",\"password\":\"mypass\"}";
        APIGatewayV2HTTPEvent event = createHttpEvent(requestBody);
        
        ValidateUserResponse mockResponse = new ValidateUserResponse("myuser", "jwt-token");
        when(loginUserUseCase.apply(any(ValidateUserCommand.class))).thenReturn(mockResponse);

        // When
        loginFunction.apply(event);

        // Then
        verify(loginUserUseCase).apply(new ValidateUserCommand("myuser", "mypass"));
    }

    private APIGatewayV2HTTPEvent createHttpEvent(String body) {
        APIGatewayV2HTTPEvent event = new APIGatewayV2HTTPEvent();
        event.setBody(body);
        return event;
    }
}
