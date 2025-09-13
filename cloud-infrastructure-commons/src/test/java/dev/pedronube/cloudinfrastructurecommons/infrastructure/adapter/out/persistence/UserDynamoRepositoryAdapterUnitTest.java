package dev.pedronube.cloudinfrastructurecommons.infrastructure.adapter.out.persistence;

import dev.pedronube.cloudinfrastructurecommons.infrastructure.adapter.out.persistence.entity.UserEntity;
import dev.pedronube.cloudinfrastructurecommons.infrastructure.adapter.out.persistence.entity.mapper.UserEntityFactory;
import dev.pedronube.domaincommons.domain.model.user.User;
import dev.pedronube.domaincommons.domain.port.out.repository.user.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserDynamoRepositoryAdapter Unit Tests")
class UserDynamoRepositoryAdapterUnitTest {

    @Mock
    private DynamoDbEnhancedClient mockClient;

    @Mock
    private DynamoDbTable<UserEntity> mockTable;

    private UserDynamoRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        when(mockClient.table(eq("serverless-back-table"), any(TableSchema.class)))
                .thenReturn(mockTable);
        adapter = new UserDynamoRepositoryAdapter(mockClient);
    }

    @Nested
    @DisplayName("Bean creation")
    class BeanCreationTests {

        @Test
        @DisplayName("Should create SaveUserPort bean")
        void shouldCreateSaveUserPortBean() {
            // When
            SaveUserPort saveUserPort = adapter.saveUserPort();

            // Then
            assertThat(saveUserPort).isNotNull();
        }

        @Test
        @DisplayName("Should create FindUserByIdPort bean")
        void shouldCreateFindUserByIdPortBean() {
            // When
            FindUserByIdPort findUserByIdPort = adapter.findUserByIdPort();

            // Then
            assertThat(findUserByIdPort).isNotNull();
        }

        @Test
        @DisplayName("Should create FindUserByEmailPort bean")
        void shouldCreateFindUserByEmailPortBean() {
            // When
            FindUserByEmailPort findUserByEmailPort = adapter.findUserByEmailPort();

            // Then
            assertThat(findUserByEmailPort).isNotNull();
        }

        @Test
        @DisplayName("Should create UpdateUserPort bean")
        void shouldCreateUpdateUserPortBean() {
            // When
            UpdateUserPort updateUserPort = adapter.updateUserPort();

            // Then
            assertThat(updateUserPort).isNotNull();
        }

        @Test
        @DisplayName("Should create DeleteUserPort bean")
        void shouldCreateDeleteUserPortBean() {
            // When
            DeleteUserPort deleteUserPort = adapter.deleteUserPort();

            // Then
            assertThat(deleteUserPort).isNotNull();
        }
    }

    @Nested
    @DisplayName("SaveUserPort functionality")
    class SaveUserPortTests {

        @Test
        @DisplayName("Should save user successfully")
        void shouldSaveUserSuccessfully() {
            // Given
            User user = new User();
            user.setSub("sub-123");
            user.setUsername("testuser");
            user.setEmail("test@example.com");
            user.setSubscriptionLevel("FREE");
            user.setCreatedAt("2024-01-01T10:00:00Z");

            SaveUserPort saveUserPort = adapter.saveUserPort();
            ArgumentCaptor<UserEntity> entityCaptor = ArgumentCaptor.forClass(UserEntity.class);

            // When
            saveUserPort.accept(user);

            // Then
            verify(mockTable).putItem(entityCaptor.capture());
            UserEntity capturedEntity = entityCaptor.getValue();
            assertThat(capturedEntity.getSub()).isEqualTo("sub-123");
            assertThat(capturedEntity.getUsername()).isEqualTo("testuser");
            assertThat(capturedEntity.getEmail()).isEqualTo("test@example.com");
        }

        @Test
        @DisplayName("Should handle null user gracefully")
        void shouldHandleNullUserGracefully() {
            // Given
            User user = null;
            SaveUserPort saveUserPort = adapter.saveUserPort();

            // When
            saveUserPort.accept(user);

            // Then
            verify(mockTable).putItem((UserEntity) null);
        }
    }

    @Nested
    @DisplayName("FindUserByIdPort functionality")
    class FindUserByIdPortTests {

        @Test
        @DisplayName("Should find user by sub successfully")
        void shouldFindUserBySubSuccessfully() {
            // Given
            String sub = "sub-123";
            UserEntity mockEntity = UserEntityFactory.fromCognitoData(sub, "testuser", "test@example.com");
            when(mockTable.getItem(any(Key.class))).thenReturn(mockEntity);

            FindUserByIdPort findUserByIdPort = adapter.findUserByIdPort();

            // When
            Optional<User> result = findUserByIdPort.apply(sub);

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getSub()).isEqualTo(sub);
            assertThat(result.get().getUsername()).isEqualTo("testuser");
            assertThat(result.get().getEmail()).isEqualTo("test@example.com");

            ArgumentCaptor<Key> keyCaptor = ArgumentCaptor.forClass(Key.class);
            verify(mockTable).getItem(keyCaptor.capture());
            // Note: Key verification would require access to Key internals
        }

        @Test
        @DisplayName("Should return empty when user not found")
        void shouldReturnEmptyWhenUserNotFound() {
            // Given
            String sub = "nonexistent-sub";
            when(mockTable.getItem(any(Key.class))).thenReturn(null);

            FindUserByIdPort findUserByIdPort = adapter.findUserByIdPort();

            // When
            Optional<User> result = findUserByIdPort.apply(sub);

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should handle null sub parameter")
        void shouldHandleNullSubParameter() {
            // Given
            String sub = null;
            when(mockTable.getItem(any(Key.class))).thenReturn(null);

            FindUserByIdPort findUserByIdPort = adapter.findUserByIdPort();

            // When
            Optional<User> result = findUserByIdPort.apply(sub);

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("FindUserByEmailPort functionality")
    class FindUserByEmailPortTests {

        @Test
        @DisplayName("Should find user by email successfully")
        void shouldFindUserByEmailSuccessfully() {
            // Given
            String email = "test@example.com";
            UserEntity mockEntity = UserEntityFactory.fromCognitoData("sub-123", "testuser", email);
            
            // Mock the findByAttribute method behavior
            UserDynamoRepositoryAdapter spyAdapter = spy(adapter);
            doReturn(List.of(mockEntity)).when(spyAdapter).findByAttribute("email", email);

            FindUserByEmailPort findUserByEmailPort = spyAdapter.findUserByEmailPort();

            // When
            Optional<User> result = findUserByEmailPort.apply(email);

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getEmail()).isEqualTo(email);
            assertThat(result.get().getSub()).isEqualTo("sub-123");
        }

        @Test
        @DisplayName("Should return empty when no user found by email")
        void shouldReturnEmptyWhenNoUserFoundByEmail() {
            // Given
            String email = "nonexistent@example.com";
            
            UserDynamoRepositoryAdapter spyAdapter = spy(adapter);
            doReturn(List.of()).when(spyAdapter).findByAttribute("email", email);

            FindUserByEmailPort findUserByEmailPort = spyAdapter.findUserByEmailPort();

            // When
            Optional<User> result = findUserByEmailPort.apply(email);

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should return first user when multiple users found by email")
        void shouldReturnFirstUserWhenMultipleUsersFoundByEmail() {
            // Given
            String email = "shared@example.com";
            UserEntity entity1 = UserEntityFactory.fromCognitoData("sub-1", "user1", email);
            UserEntity entity2 = UserEntityFactory.fromCognitoData("sub-2", "user2", email);
            
            UserDynamoRepositoryAdapter spyAdapter = spy(adapter);
            doReturn(List.of(entity1, entity2)).when(spyAdapter).findByAttribute("email", email);

            FindUserByEmailPort findUserByEmailPort = spyAdapter.findUserByEmailPort();

            // When
            Optional<User> result = findUserByEmailPort.apply(email);

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getSub()).isEqualTo("sub-1");
        }
    }

    @Nested
    @DisplayName("UpdateUserPort functionality")
    class UpdateUserPortTests {

        @Test
        @DisplayName("Should update user successfully")
        void shouldUpdateUserSuccessfully() {
            // Given
            User user = new User();
            user.setSub("sub-123");
            user.setUsername("updateduser");
            user.setEmail("updated@example.com");
            user.setSubscriptionLevel("PREMIUM");

            UpdateUserPort updateUserPort = adapter.updateUserPort();
            ArgumentCaptor<UserEntity> entityCaptor = ArgumentCaptor.forClass(UserEntity.class);

            // When
            updateUserPort.accept(user);

            // Then
            verify(mockTable).putItem(entityCaptor.capture());
            UserEntity capturedEntity = entityCaptor.getValue();
            assertThat(capturedEntity.getSub()).isEqualTo("sub-123");
            assertThat(capturedEntity.getUsername()).isEqualTo("updateduser");
            assertThat(capturedEntity.getEmail()).isEqualTo("updated@example.com");
        }
    }

    @Nested
    @DisplayName("DeleteUserPort functionality")
    class DeleteUserPortTests {

        @Test
        @DisplayName("Should delete user by sub successfully")
        void shouldDeleteUserBySubSuccessfully() {
            // Given
            String sub = "sub-to-delete";
            DeleteUserPort deleteUserPort = adapter.deleteUserPort();

            // When
            deleteUserPort.accept(sub);

            // Then
            verify(mockTable).deleteItem(any(Key.class));
        }

        @Test
        @DisplayName("Should handle null sub parameter")
        void shouldHandleNullSubParameter() {
            // Given
            String sub = null;
            DeleteUserPort deleteUserPort = adapter.deleteUserPort();

            // When
            deleteUserPort.accept(sub);

            // Then
            verify(mockTable).deleteItem(any(Key.class));
        }
    }

    @Nested
    @DisplayName("Table schema")
    class TableSchemaTests {

        @Test
        @DisplayName("Should return correct table schema")
        void shouldReturnCorrectTableSchema() {
            // When
            TableSchema<UserEntity> schema = adapter.getTableSchema();

            // Then
            assertThat(schema).isNotNull();
            assertThat(schema.itemType().rawClass()).isEqualTo(UserEntity.class);
        }
    }
}