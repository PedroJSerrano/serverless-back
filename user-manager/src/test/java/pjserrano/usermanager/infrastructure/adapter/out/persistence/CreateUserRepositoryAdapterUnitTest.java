package pjserrano.usermanager.infrastructure.adapter.out.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pjserrano.usermanager.domain.port.out.CreateUserRepositoryPort;
import pjserrano.usermanager.domain.port.out.DeleteUserRepositoryPort;
import pjserrano.usermanager.domain.port.out.UpdateUserRepositoryPort;
import pjserrano.usermanager.infrastructure.adapter.out.persistence.model.UserDynamoEntity;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateUserRepositoryAdapterUnitTest {

    @Mock
    private DynamoDbEnhancedClient mockEnhancedClient;

    @Mock
    private DynamoDbTable<UserDynamoEntity> mockUserTable;

    private DynamoDbUserRepositoryAdapter adapter;
    private CreateUserRepositoryPort createUserRepositoryPort;

    @BeforeEach
    void setUp() {
        adapter = new DynamoDbUserRepositoryAdapter(mockEnhancedClient);

        when(mockEnhancedClient.table(any(String.class), any())).thenReturn((DynamoDbTable) mockUserTable);

        createUserRepositoryPort = adapter.createUserRepositoryPort();
    }

    @Test
    void whenUserCreated_thenShouldReturnUser() {

    }
}
