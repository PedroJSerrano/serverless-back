package pjserrano.login.infrastructure.adapter.out.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pjserrano.login.application.port.out.JwtSecretProviderPort;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;
import software.amazon.awssdk.services.ssm.model.Parameter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SsmJwtSecretProviderAdapterTest {

    @Mock
    private SsmClient mockSsmClient;

    private SsmJwtSecretProviderAdapter adapter;
    private JwtSecretProviderPort jwtSecretProvider;

    @BeforeEach
    void setUp() {
        // Creamos el adaptador con el SsmClient mockeado
        adapter = new SsmJwtSecretProviderAdapter(mockSsmClient);
        // Obtenemos el bean configurado por el adaptador
        jwtSecretProvider = adapter.jwtSecretProvider();
    }

    @Test
    void shouldReturnSecretValueWhenParameterExists() {
        // GIVEN: Un valor de secreto esperado y una respuesta mockeada de SSM
        String expectedSecretValue = "my-test-secret-12345";
        Parameter mockParameter = Parameter.builder()
                .name("/login/jwt/secret")
                .value(expectedSecretValue)
                .build();
        GetParameterResponse mockResponse = GetParameterResponse.builder()
                .parameter(mockParameter)
                .build();

        // WHEN: Configuramos el mock para que devuelva la respuesta esperada
        when(mockSsmClient.getParameter(any(GetParameterRequest.class))).thenReturn(mockResponse);

        // THEN: Cuando llamamos al supplier, debe devolver el valor esperado
        String actualSecret = jwtSecretProvider.get();
        assertEquals(expectedSecretValue, actualSecret);
    }

    @Test
    void shouldThrowExceptionWhenSsmReturnsNull() {
        // GIVEN: SSM devuelve null (caso edge)
        when(mockSsmClient.getParameter(any(GetParameterRequest.class))).thenReturn(null);

        // WHEN & THEN: Verificamos que lance excepción por Optional.orElseThrow
        RuntimeException exception = assertThrows(RuntimeException.class, () -> jwtSecretProvider.get());
        assertEquals("No se pudo obtener el secreto JWT", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSsmFails() {
        // GIVEN: Una excepción que el cliente de SSM simula
        when(mockSsmClient.getParameter(any(GetParameterRequest.class)))
                .thenThrow(new RuntimeException("Simulated AWS error"));

        // WHEN & THEN: Verificamos que el method lance una excepción
        assertThrows(RuntimeException.class, () -> jwtSecretProvider.get());
    }
}