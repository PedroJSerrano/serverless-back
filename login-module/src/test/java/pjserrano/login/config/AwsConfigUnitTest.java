package pjserrano.login.config;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.ssm.SsmClient;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios que prueban la configuración de forma aislada.
 * No requieren contexto de Spring ni infraestructura externa.
 */
class AwsConfigUnitTest {

    // Verifica que la configuración puede crear SsmClient directamente
    @Test
    void awsConfigCreatesSsmClientDirectly() {
        AwsConfig config = new AwsConfig();
        SsmClient client = config.ssmClient();
        
        assertNotNull(client);
        assertEquals("ssm", client.serviceName());
    }
}