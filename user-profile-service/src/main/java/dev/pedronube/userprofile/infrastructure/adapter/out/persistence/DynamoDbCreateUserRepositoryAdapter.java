package dev.pedronube.userprofile.infrastructure.adapter.out.persistence;

import dev.pedronube.userprofile.domain.port.out.CreateUserRepositoryPort;
import dev.pedronube.userprofile.infrastructure.adapter.out.persistence.model.UserDynamoEntity;
import dev.pedronube.userprofile.infrastructure.adapter.out.persistence.utils.UserUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Configuration
@RequiredArgsConstructor
public class DynamoDbCreateUserRepositoryAdapter {

    private final DynamoDbEnhancedClient enhancedClient;

    @Bean
    public DynamoDbTable<UserDynamoEntity> userTable() {
        return enhancedClient.table("users", TableSchema.fromBean(UserDynamoEntity.class));
    }

    @Bean
    public CreateUserRepositoryPort createUserRepositoryPort() {
        DynamoDbTable<UserDynamoEntity> userTable = userTable();
        return userInput -> userInput.flatMap(user -> {
            userTable.putItem(UserUtility.mapUserToEntity(user));
            return UserUtility.mapUserFromEntity(UserUtility.getUser(userTable, user.getUsername()));
        });
    }
}
