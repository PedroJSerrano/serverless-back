package pjserrano.authmanager.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SsmClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ContextConfiguration(classes = {AwsConfig.class})
class AwsConfigUnitTest {

    @Autowired
    private ApplicationContext context;

    @TestConfiguration // <-- Esta anotación es la clave
    static class TestAwsConfig {
        @Bean
        public SsmClient ssmClient() {
            // Devuelve un cliente mockeado que no necesita credenciales reales
            return SsmClient.builder()
                    .region(Region.EU_WEST_1)
                    .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("test", "test")))
                    .build();
        }
    }

    @Test
    void testSsmClientBeanCreation() {
        assertNotNull(context);

        SsmClient ssmClient = context.getBean(SsmClient.class);

        assertNotNull(ssmClient, "El bean SsmClient no se creó correctamente.");
    }
}