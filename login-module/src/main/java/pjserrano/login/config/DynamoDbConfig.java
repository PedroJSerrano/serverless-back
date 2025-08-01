package pjserrano.login.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;

import java.net.URI;

@Configuration
public class DynamoDbConfig {

    // Las propiedades se inyectan solo si están definidas
    @Value("${aws.dynamodb.endpoint:#{null}}")
    private URI endpoint;

    @Value("${aws.region:#{null}}")
    private String region;

    @Value("${aws.accessKeyId:#{null}}")
    private String accessKey;

    @Value("${aws.secretAccessKey:#{null}}")
    private String secretKey;

    @Bean
    public DynamoDbClient dynamoDbClient() {
        // La clase del builder es DynamoDbClientBuilder
        DynamoDbClientBuilder builder = DynamoDbClient.builder();

        // El resto del código es correcto
        if (endpoint != null && region != null && accessKey != null && secretKey != null) {
            builder = builder
                    .endpointOverride(endpoint)
                    .region(Region.of(region))
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(accessKey, secretKey)
                    ));
        }

        return builder.build();
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }
}