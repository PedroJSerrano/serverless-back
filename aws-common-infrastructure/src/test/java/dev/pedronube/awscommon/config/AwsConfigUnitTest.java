package dev.pedronube.awscommon.config;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SsmClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AwsConfigUnitTest {

    @Test
    void testSsmClientBeanCreation() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        context.register(TestConfig.class);

        context.refresh();

        SsmClient ssmClient = context.getBean(SsmClient.class);
        assertNotNull(ssmClient, "El bean SsmClient no se creó correctamente.");

        context.close();
    }

    private static class TestConfig {
        @Bean
        public SsmClient ssmClient() {
            return SsmClient.builder()
                    .region(Region.EU_WEST_1)
                    .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("test", "test")))
                    .build();
        }
    }
}