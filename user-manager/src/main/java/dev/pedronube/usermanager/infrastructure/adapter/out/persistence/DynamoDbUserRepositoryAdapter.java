package dev.pedronube.usermanager.infrastructure.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import dev.pedronube.usermanager.domain.model.User;
import dev.pedronube.usermanager.domain.port.out.CreateUserRepositoryPort;
import dev.pedronube.usermanager.domain.port.out.DeleteUserRepositoryPort;
import dev.pedronube.usermanager.domain.port.out.UpdateUserRepositoryPort;
import dev.pedronube.usermanager.infrastructure.adapter.out.persistence.model.UserDynamoEntity;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.List;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class DynamoDbUserRepositoryAdapter {

    private final DynamoDbEnhancedClient enhancedClient;

    @Bean
    public DynamoDbTable<UserDynamoEntity> userTable() {
        return enhancedClient.table("users", TableSchema.fromBean(UserDynamoEntity.class));
    }

    @Bean
    public CreateUserRepositoryPort createUserRepositoryPort() {
        DynamoDbTable<UserDynamoEntity> userTable = userTable();
        return userInput -> userInput.flatMap(user -> {
            userTable.putItem(mapUserToEntity(user));
            return mapUserFromEntity(getUser(userTable, user.getUsername()));
        });
    }

    @Bean
    public UpdateUserRepositoryPort updateUserRepositoryPort() {
        DynamoDbTable<UserDynamoEntity> userTable = userTable();
        return userInput -> userInput.flatMap(user -> {
            userTable.updateItem(mapUserToEntity(user));
            return mapUserFromEntity(getUser(userTable, user.getUsername()));
        });
    }

    @Bean
    public DeleteUserRepositoryPort deleteUserRepositoryPort() {
        DynamoDbTable<UserDynamoEntity> userTable = userTable();
        return username -> Optional.ofNullable(getUser(userTable, username))
                .map(entity -> {
                    userTable.deleteItem(entity);
                    return getUser(userTable, username) == null;
                })
                .orElse(false);
    }

    private UserDynamoEntity getUser(DynamoDbTable<UserDynamoEntity> userTable, String username) {
        Key key = Key.builder().partitionValue(username).build();
        return userTable.getItem(key);
    }

    private Optional<User> mapUserFromEntity(UserDynamoEntity entity) {
        return Optional.ofNullable(entity)
                .map(ent -> new User(
                        ent.getUsername(),
                        ent.getPassword(),
                        ent.getEmail(),
                        ent.getRoles() != null ? new java.util.ArrayList<>(ent.getRoles()) : List.of()
                ));
    }

    private UserDynamoEntity mapUserToEntity(User user) {
        return new UserDynamoEntity(
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getRoles()
        );
    }
}