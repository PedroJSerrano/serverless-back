package pjserrano.awscommon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.ssm.SsmClient;

/**
 * Configuración de clientes AWS.
 * Spring Cloud AWS maneja automáticamente credenciales y región,
 * pero necesitamos crear los beans manualmente.
 */
@Configuration
public class AwsConfig {

    @Bean
    public SsmClient ssmClient() {
        // Spring Cloud AWS configura automáticamente credenciales y región
        return SsmClient.builder().build();
    }
}