package dev.pedronube.cloudinfrastructurecommons.infrastructure.adapter.out.persistence.entity;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@DynamoDbBean
public class UserEntity extends BaseEntity {
    private String sub; // El ID de Cognito
    private String username;
    private String email;
    private String subscriptionLevel;
    private String createdAt;
}