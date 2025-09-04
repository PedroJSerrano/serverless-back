package dev.pedronube.usermanager.infrastructure.adapter.out.persistence;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import dev.pedronube.usermanager.domain.port.out.UpdateUserRepositoryPort;
import dev.pedronube.usermanager.infrastructure.adapter.out.persistence.model.UserDynamoEntity;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

@ExtendWith(MockitoExtension.class)
public class UpdateUserRepositoryAdapterUnitTest {

    @Mock
    private DynamoDbEnhancedClient mockEnhancedClient;

    @Mock
    private DynamoDbTable<UserDynamoEntity> mockUserTable;

    private DynamoDbUserRepositoryAdapter adapter;
    private UpdateUserRepositoryPort updateUserRepositoryPort;
}
