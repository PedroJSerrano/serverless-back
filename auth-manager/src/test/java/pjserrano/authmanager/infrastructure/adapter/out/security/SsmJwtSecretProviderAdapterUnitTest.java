package pjserrano.authmanager.infrastructure.adapter.out.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pjserrano.authmanager.domain.port.out.JwtSecretProviderPort;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;
import software.amazon.awssdk.services.ssm.model.Parameter;
import software.amazon.awssdk.services.ssm.model.ParameterNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios con mocks para verificar la lógica del proveedor de secretos JWT.
 * Prueban el comportamiento del adaptador SSM de forma aislada.
 */
@ExtendWith(MockitoExtension.class)
class SsmJwtSecretProviderAdapterUnitTest {

    @Mock
    private SsmClient mockSsmClient;

    private SsmJwtSecretProviderAdapter adapter;
    private JwtSecretProviderPort jwtSecretProvider;

    @BeforeEach
    void setUp() {
        adapter = new SsmJwtSecretProviderAdapter(mockSsmClient);
        jwtSecretProvider = adapter.jwtSecretProvider();
    }

    @Test
    void shouldReturnSecretValueWhenParameterExists() {
        String expectedSecretValue = "my-test-secret-12345";
        GetParameterResponse mockResponse = GetParameterResponse.builder()
                .parameter(p -> p.name("/login/jwt/secret").value(expectedSecretValue))
                .build();

        when(mockSsmClient.getParameter(any(GetParameterRequest.class))).thenReturn(mockResponse);

        String actualSecret = jwtSecretProvider.get();

        assertEquals(expectedSecretValue, actualSecret);
    }

    @Test
    void shouldConfigureRequestWithDecryption() {
        GetParameterResponse mockResponse = GetParameterResponse.builder()
                .parameter(p -> p.name("/login/jwt/secret").value("test-secret"))
                .build();

        when(mockSsmClient.getParameter(any(GetParameterRequest.class))).thenReturn(mockResponse);

        jwtSecretProvider.get();

        ArgumentCaptor<GetParameterRequest> requestCaptor = ArgumentCaptor.forClass(GetParameterRequest.class);
        verify(mockSsmClient).getParameter(requestCaptor.capture());
        
        GetParameterRequest capturedRequest = requestCaptor.getValue();
        assertEquals("/login/jwt/secret", capturedRequest.name());
        assertTrue(capturedRequest.withDecryption(), "Request should have withDecryption=true");
    }

    @Test
    void shouldThrowExceptionWhenSsmReturnsNull() {
        when(mockSsmClient.getParameter(any(GetParameterRequest.class))).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> jwtSecretProvider.get());
        assertEquals("No se pudo obtener el secreto JWT", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenParameterNotFound() {
        when(mockSsmClient.getParameter(any(GetParameterRequest.class)))
                .thenThrow(ParameterNotFoundException.builder()
                        .message("Parameter /login/jwt/secret not found")
                        .build());

        assertThrows(ParameterNotFoundException.class, () -> jwtSecretProvider.get());
    }

    @Test
    void shouldThrowExceptionWhenSsmFails() {
        when(mockSsmClient.getParameter(any(GetParameterRequest.class)))
                .thenThrow(new RuntimeException("Simulated AWS error"));

        assertThrows(RuntimeException.class, () -> jwtSecretProvider.get());
    }

    @Test
    void shouldThrowExceptionWhenResponseHasNullParameter() {
        GetParameterResponse mockResponse = GetParameterResponse.builder()
                .parameter((Parameter) null)
                .build();

        when(mockSsmClient.getParameter(any(GetParameterRequest.class))).thenReturn(mockResponse);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> jwtSecretProvider.get());
        assertEquals("No se pudo obtener el secreto JWT", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenParameterHasNullValue() {
        GetParameterResponse mockResponse = GetParameterResponse.builder()
                .parameter(p -> p.name("/login/jwt/secret").value(null))
                .build();

        when(mockSsmClient.getParameter(any(GetParameterRequest.class))).thenReturn(mockResponse);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> jwtSecretProvider.get());
        assertEquals("No se pudo obtener el secreto JWT", exception.getMessage());
    }

    @Test
    void shouldReturnEmptyStringWhenParameterHasEmptyValue() {
        GetParameterResponse mockResponse = GetParameterResponse.builder()
                .parameter(p -> p.name("/login/jwt/secret").value(""))
                .build();

        when(mockSsmClient.getParameter(any(GetParameterRequest.class))).thenReturn(mockResponse);

        String actualSecret = jwtSecretProvider.get();

        assertEquals("", actualSecret);
    }

    @Test
    void shouldWorkWhenCalledMultipleTimes() {
        String expectedSecretValue = "consistent-secret-value";
        GetParameterResponse mockResponse = GetParameterResponse.builder()
                .parameter(p -> p.name("/login/jwt/secret").value(expectedSecretValue))
                .build();

        when(mockSsmClient.getParameter(any(GetParameterRequest.class))).thenReturn(mockResponse);

        String secret1 = jwtSecretProvider.get();
        String secret2 = jwtSecretProvider.get();
        String secret3 = jwtSecretProvider.get();

        assertEquals(expectedSecretValue, secret1);
        assertEquals(expectedSecretValue, secret2);
        assertEquals(expectedSecretValue, secret3);

        verify(mockSsmClient, times(3)).getParameter(any(GetParameterRequest.class));
    }
}