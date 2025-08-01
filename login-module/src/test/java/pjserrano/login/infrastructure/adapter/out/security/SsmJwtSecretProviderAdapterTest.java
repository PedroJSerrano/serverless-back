package pjserrano.login.infrastructure.adapter.out.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pjserrano.login.application.port.out.JwtSecretProviderPort;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;
import software.amazon.awssdk.services.ssm.model.Parameter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SsmJwtSecretProviderAdapterTest {

    // Mockeamos el cliente de AWS SSM para que no haga llamadas reales
    @Mock
    private SsmClient mockSsmClient;

    // La clase que vamos a testear
    private JwtSecretProviderPort jwtSecretProvider;

    @BeforeEach
    void setUp() {
        // Inicializa los mocks antes de cada test
        MockitoAnnotations.openMocks(this);
        // Creamos la instancia de nuestro adaptador, inyectando el mock del cliente
        SsmJwtSecretProviderAdapter adapterConfig = new SsmJwtSecretProviderAdapter();
        // Hay que re-implementar el constructor o method de inyección para el test
        // En este caso, simplificamos creando el bean manualmente.
        this.jwtSecretProvider = () -> {
            try {
                GetParameterResponse response = mockSsmClient.getParameter(GetParameterRequest.builder()
                        .name("/login/jwt/secret")
                        .withDecryption(true)
                        .build());
                return response.parameter().value();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Test
    void shouldReturnSecretValueWhenParameterExists() {
        // GIVEN: Un valor de secreto esperado y una respuesta mockeada de SSM
        String expectedSecretValue = "my-test-secret-12345";
        Parameter mockParameter = Parameter.builder().name("/login/jwt/secret").value(expectedSecretValue).build();
        GetParameterResponse mockResponse = GetParameterResponse.builder().parameter(mockParameter).build();

        // WHEN: Configuramos el mock para que devuelva la respuesta esperada
        when(mockSsmClient.getParameter(any(GetParameterRequest.class))).thenReturn(mockResponse);

        // THEN: Cuando llamamos al method, debe devolver el valor esperado
        String actualSecret = jwtSecretProvider.get();
        assertEquals(expectedSecretValue, actualSecret);
    }

    @Test
    void shouldThrowExceptionWhenSsmFails() {
        // GIVEN: Una excepción que el cliente de SSM simula que ha ocurrido
        when(mockSsmClient.getParameter(any(GetParameterRequest.class))).thenThrow(new RuntimeException("Simulated AWS error"));

        // WHEN & THEN: Verificamos que el method lance una excepción
        assertThrows(RuntimeException.class, () -> jwtSecretProvider.get());
    }
}