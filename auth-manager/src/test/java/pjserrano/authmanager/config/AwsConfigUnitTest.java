package pjserrano.authmanager.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import software.amazon.awssdk.services.ssm.SsmClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ContextConfiguration(classes = {AwsConfig.class})
class AwsConfigUnitTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void testSsmClientBeanCreation() {
        assertNotNull(context);

        SsmClient ssmClient = context.getBean(SsmClient.class);

        assertNotNull(ssmClient, "El bean SsmClient no se creó correctamente.");
    }
}