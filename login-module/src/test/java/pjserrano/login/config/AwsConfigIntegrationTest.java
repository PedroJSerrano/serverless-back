package pjserrano.login.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.services.ssm.SsmClient;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de integración con LocalStack para simular servicios AWS.
 * Verifica que SsmClient funciona correctamente con infraestructura local.
 */
@Testcontainers
@SpringBootTest(classes = AwsConfig.class)
class AwsConfigIntegrationTest {

    static Logger logger = Logger.getLogger(AwsConfigIntegrationTest.class.getName());

    private static final DockerImageName LOCALSTACK_IMAGE = DockerImageName.parse("localstack/localstack:latest");

    @Container
    public static LocalStackContainer localstack =
            new LocalStackContainer(LOCALSTACK_IMAGE)
                    .withServices(LocalStackContainer.Service.SSM);

    @DynamicPropertySource
    static void registerAwsProperties(DynamicPropertyRegistry registry) {
        // Propiedades estándar de Spring Cloud AWS
        registry.add("spring.cloud.aws.ssm.endpoint", () -> localstack.getEndpointOverride(LocalStackContainer.Service.SSM).toString());
        registry.add("spring.cloud.aws.region.static", () -> localstack.getRegion());
        registry.add("spring.cloud.aws.credentials.access-key", () -> localstack.getAccessKey());
        registry.add("spring.cloud.aws.credentials.secret-key", () -> localstack.getSecretKey());
        logger.info("Spring Cloud AWS properties injected for LocalStack SSM.");
    }

    @Autowired
    private SsmClient ssmClient;

    @Autowired
    private ApplicationContext applicationContext;

    // Verifica que Spring puede cargar el contexto y crear el bean SsmClient
    @Test
    void contextLoadsAndSsmClientIsAutoConfiguredForLocalStack() {
        assertNotNull(ssmClient, "SsmClient should be auto-configured by Spring Cloud AWS");
        logger.info("LocalStack test passed - SsmClient auto-configured successfully");
    }

    // Confirma que el bean SsmClient está registrado en el contexto de Spring
    @Test
    void ssmClientBeanIsRegistered() {
        assertTrue(applicationContext.containsBean("ssmClient"));
        assertSame(ssmClient, applicationContext.getBean("ssmClient"));
    }

    // Verifica que el SsmClient se puede obtener por tipo desde el contexto
    @Test
    void ssmClientCanBeRetrievedByType() {
        SsmClient clientByType = applicationContext.getBean(SsmClient.class);
        assertSame(ssmClient, clientByType);
    }

    // Verifica que la clase de configuración está cargada como bean
    @Test
    void configurationClassIsLoaded() {
        assertTrue(applicationContext.containsBean("awsConfig"));
        Object configBean = applicationContext.getBean("awsConfig");
        assertTrue(configBean instanceof AwsConfig, "Bean should be instance of AwsConfig");
    }
}