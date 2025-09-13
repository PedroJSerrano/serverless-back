package dev.pedronube.userprofile.infrastructure.adapter.out.persistence.utils;

import dev.pedronube.userprofile.domain.model.User;
import dev.pedronube.userprofile.infrastructure.adapter.out.persistence.model.UserDynamoEntity;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.List;
import java.util.Optional;

public class UserUtility {

    private UserUtility() {}

    public static Optional<User> mapUserFromEntity(UserDynamoEntity entity) {
        return Optional.ofNullable(entity)
                .map(ent -> new User(
                        ent.getUsername(),
                        ent.getPassword(),
                        ent.getEmail(),
                        ent.getRoles() != null ? new java.util.ArrayList<>(ent.getRoles()) : List.of()
                ));
    }

    public static UserDynamoEntity mapUserToEntity(User user) {
        return new UserDynamoEntity(
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getRoles()
        );
    }

    public static UserDynamoEntity getUser(DynamoDbTable<UserDynamoEntity> userTable, String username) {
        Key key = Key.builder().partitionValue(username).build();
        return userTable.getItem(key);
    }
}
