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

    // Verifica que retorna el valor del secreto cuando el parámetro existe
    @Test
    void shouldReturnSecretValueWhenParameterExists() {
        // GIVEN
        String expectedSecretValue = "my-test-secret-12345";
        GetParameterResponse mockResponse = GetParameterResponse.builder()
                .parameter(p -> p.name("/login/jwt/secret").value(expectedSecretValue))
                .build();

        when(mockSsmClient.getParameter(any(GetParameterRequest.class))).thenReturn(mockResponse);

        // WHEN
        String actualSecret = jwtSecretProvider.get();

        // THEN
        assertEquals(expectedSecretValue, actualSecret);
    }

    // Verifica que se configura correctamente el request con decryption
    @Test
    void shouldConfigureRequestWithDecryption() {
        // GIVEN
        GetParameterResponse mockResponse = GetParameterResponse.builder()
                .parameter(p -> p.name("/login/jwt/secret").value("test-secret"))
                .build();

        when(mockSsmClient.getParameter(any(GetParameterRequest.class))).thenReturn(mockResponse);

        // WHEN
        jwtSecretProvider.get();

        // THEN
        ArgumentCaptor<GetParameterRequest> requestCaptor = ArgumentCaptor.forClass(GetParameterRequest.class);
        verify(mockSsmClient).getParameter(requestCaptor.capture());
        
        GetParameterRequest capturedRequest = requestCaptor.getValue();
        assertEquals("/login/jwt/secret", capturedRequest.name());
        assertTrue(capturedRequest.withDecryption(), "Request should have withDecryption=true");
    }

    // Verifica que lanza excepción cuando SSM retorna null
    @Test
    void shouldThrowExceptionWhenSsmReturnsNull() {
        // GIVEN
        when(mockSsmClient.getParameter(any(GetParameterRequest.class))).thenReturn(null);

        // WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> jwtSecretProvider.get());
        assertEquals("No se pudo obtener el secreto JWT", exception.getMessage());
    }

    // Verifica que lanza excepción cuando el parámetro no existe
    @Test
    void shouldThrowExceptionWhenParameterNotFound() {
        // GIVEN
        when(mockSsmClient.getParameter(any(GetParameterRequest.class)))
                .thenThrow(ParameterNotFoundException.builder()
                        .message("Parameter /login/jwt/secret not found")
                        .build());

        // WHEN & THEN
        assertThrows(ParameterNotFoundException.class, () -> jwtSecretProvider.get());
    }

    // Verifica que lanza excepción cuando SSM falla
    @Test
    void shouldThrowExceptionWhenSsmFails() {
        // GIVEN
        when(mockSsmClient.getParameter(any(GetParameterRequest.class)))
                .thenThrow(new RuntimeException("Simulated AWS error"));

        // WHEN & THEN
        assertThrows(RuntimeException.class, () -> jwtSecretProvider.get());
    }

    // Verifica que maneja respuesta con parámetro nulo
    @Test
    void shouldThrowExceptionWhenResponseHasNullParameter() {
        // GIVEN
        GetParameterResponse mockResponse = GetParameterResponse.builder()
                .parameter((Parameter) null)
                .build();

        when(mockSsmClient.getParameter(any(GetParameterRequest.class))).thenReturn(mockResponse);

        // WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> jwtSecretProvider.get());
        assertEquals("No se pudo obtener el secreto JWT", exception.getMessage());
    }

    // Verifica que maneja parámetro con valor nulo
    @Test
    void shouldThrowExceptionWhenParameterHasNullValue() {
        // GIVEN
        GetParameterResponse mockResponse = GetParameterResponse.builder()
                .parameter(p -> p.name("/login/jwt/secret").value(null))
                .build();

        when(mockSsmClient.getParameter(any(GetParameterRequest.class))).thenReturn(mockResponse);

        // WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> jwtSecretProvider.get());
        assertEquals("No se pudo obtener el secreto JWT", exception.getMessage());
    }

    // Verifica que maneja parámetro con valor vacío
    @Test
    void shouldReturnEmptyStringWhenParameterHasEmptyValue() {
        // GIVEN
        GetParameterResponse mockResponse = GetParameterResponse.builder()
                .parameter(p -> p.name("/login/jwt/secret").value(""))
                .build();

        when(mockSsmClient.getParameter(any(GetParameterRequest.class))).thenReturn(mockResponse);

        // WHEN
        String actualSecret = jwtSecretProvider.get();

        // THEN
        assertEquals("", actualSecret);
    }

    // Verifica que el proveedor se puede llamar múltiples veces
    @Test
    void shouldWorkWhenCalledMultipleTimes() {
        // GIVEN
        String expectedSecretValue = "consistent-secret-value";
        GetParameterResponse mockResponse = GetParameterResponse.builder()
                .parameter(p -> p.name("/login/jwt/secret").value(expectedSecretValue))
                .build();

        when(mockSsmClient.getParameter(any(GetParameterRequest.class))).thenReturn(mockResponse);

        // WHEN
        String secret1 = jwtSecretProvider.get();
        String secret2 = jwtSecretProvider.get();
        String secret3 = jwtSecretProvider.get();

        // THEN
        assertEquals(expectedSecretValue, secret1);
        assertEquals(expectedSecretValue, secret2);
        assertEquals(expectedSecretValue, secret3);
        
        // Verificar que SSM fue llamado 3 veces
        verify(mockSsmClient, times(3)).getParameter(any(GetParameterRequest.class));
    }
}