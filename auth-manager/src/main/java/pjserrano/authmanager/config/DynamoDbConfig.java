package pjserrano.authmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

/**
 * Configuración de DynamoDB simplificada.
 * Spring Cloud AWS maneja automáticamente credenciales y región,
 * pero necesitamos crear los beans manualmente.
 */
@Configuration
public class DynamoDbConfig {

    @Bean
    public DynamoDbClient dynamoDbClient() {
        // Spring Cloud AWS configura automáticamente credenciales y región
        return DynamoDbClient.builder().build();
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }
}