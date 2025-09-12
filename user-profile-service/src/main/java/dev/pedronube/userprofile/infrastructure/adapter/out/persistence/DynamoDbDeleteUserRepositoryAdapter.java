package dev.pedronube.userprofile.infrastructure.adapter.out.persistence;

import dev.pedronube.userprofile.infrastructure.adapter.out.persistence.utils.UserUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import dev.pedronube.userprofile.domain.port.out.DeleteUserRepositoryPort;
import dev.pedronube.userprofile.infrastructure.adapter.out.persistence.model.UserDynamoEntity;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class DynamoDbDeleteUserRepositoryAdapter {

    private final DynamoDbEnhancedClient enhancedClient;

    @Bean
    public DynamoDbTable<UserDynamoEntity> userTable() {
        return enhancedClient.table("users", TableSchema.fromBean(UserDynamoEntity.class));
    }

    @Bean
    public DeleteUserRepositoryPort deleteUserRepositoryPort() {
        DynamoDbTable<UserDynamoEntity> userTable = userTable();
        return username -> Optional.ofNullable(UserUtility.getUser(userTable, username))
                .map(entity -> {
                    userTable.deleteItem(entity);
                    return UserUtility.getUser(userTable, username) == null;
                })
                .orElse(false);
    }

}