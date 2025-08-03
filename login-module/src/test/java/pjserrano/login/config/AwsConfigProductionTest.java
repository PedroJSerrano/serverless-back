package pjserrano.login.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import software.amazon.awssdk.services.ssm.SsmClient;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de producción que usan configuración AWS por defecto.
 * Verifica comportamiento sin LocalStack, usando credenciales reales.
 */
@SpringBootTest(classes = AwsConfig.class)
class AwsConfigProductionTest {

    static Logger logger = Logger.getLogger(AwsConfigProductionTest.class.getName());

    @Autowired
    private SsmClient ssmClient;

    @Autowired
    private ApplicationContext applicationContext;

    // Verifica que la configuración funciona en entorno de producción
    @Test
    void contextLoadsAndSsmClientIsAutoConfiguredForProduction() {
        assertNotNull(ssmClient, "SsmClient should be auto-configured by Spring Cloud AWS");
        logger.info("Production test passed - SsmClient auto-configured with default AWS settings");
    }

    // Confirma que el SsmClient es singleton (una sola instancia por contexto)
    @Test
    void ssmClientIsSingleton() {
        SsmClient client1 = applicationContext.getBean(SsmClient.class);
        SsmClient client2 = applicationContext.getBean(SsmClient.class);
        assertSame(client1, client2, "SsmClient should be singleton");
    }

    // Verifica que el cliente tiene el nombre de servicio correcto
    @Test
    void ssmClientHasCorrectServiceName() {
        assertEquals("ssm", ssmClient.serviceName());
    }

    // Confirma que el bean de configuración existe y es accesible
    @Test
    void awsConfigBeanExists() {
        AwsConfig config = applicationContext.getBean(AwsConfig.class);
        assertNotNull(config);
    }
}