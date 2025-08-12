package pjserrano.authmanager.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import software.amazon.awssdk.services.ssm.SsmClient;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios que prueban la configuración de forma aislada.
 * No requieren contexto de Spring ni infraestructura externa.
 */
class AwsConfigUnitTest {

    // Verifica que la configuración existe y tiene el method correcto
    @Test
    void awsConfigClassExists() {
        AwsConfig config = new AwsConfig();
        assertNotNull(config);
        
        // Verificar que el method existe usando reflexión
        try {
            var method = config.getClass().getMethod("ssmClient");
            assertNotNull(method);
            assertEquals(SsmClient.class, method.getReturnType());
        } catch (NoSuchMethodException e) {
            fail("El método ssmClient() debería existir");
        }
    }
}