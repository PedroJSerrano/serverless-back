package dev.pedronube.usermanager.infrastructure.adapter.out.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean // Anotación clave para DynamoDB
public class UserDynamoEntity {

    private String username;
    private String password;
    private String email;
    private List<String> roles;

    @DynamoDbPartitionKey
    public String getUsername() {
        return username;
    }

}