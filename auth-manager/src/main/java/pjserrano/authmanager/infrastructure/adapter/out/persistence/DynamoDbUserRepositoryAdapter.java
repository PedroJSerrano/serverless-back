package pjserrano.authmanager.infrastructure.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pjserrano.authmanager.domain.port.out.UserRepositoryPort; // La interfaz funcional
import pjserrano.authmanager.domain.model.User;
import pjserrano.authmanager.infrastructure.adapter.out.persistence.model.UserDynamoEntity;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Configuration
@RequiredArgsConstructor
public class DynamoDbUserRepositoryAdapter {

    private final DynamoDbEnhancedClient enhancedClient;
    Logger logger = Logger.getLogger(getClass().getName());

    @Bean
    public DynamoDbTable<UserDynamoEntity> userTable() {
        return enhancedClient.table("users", TableSchema.fromBean(UserDynamoEntity.class));
    }

    @Bean
    public UserRepositoryPort userRepositoryPort() {
        DynamoDbTable<UserDynamoEntity> userTable = userTable();

        // Retornamos directamente la función (lambda) que implementa el puerto
        return username -> {
            try {
                Key key = Key.builder().partitionValue(username).build();
                UserDynamoEntity entity = userTable.getItem(key);

                if (entity == null) {
                    return Optional.empty();
                }

                User user = new User(
                        entity.getUsername(),
                        entity.getPassword(),
                        entity.getRoles() != null ? new java.util.ArrayList<>(entity.getRoles()) : List.of()
                );

                return Optional.of(user);
            } catch (Exception e) {
                logger.severe("Error al buscar usuario en DynamoDB: " + e.getMessage());
                return Optional.empty();
            }
        };
    }
}