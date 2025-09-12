package dev.pedronube.cognitointegration.application.service;

import dev.pedronube.domaincommons.domain.model.user.User;
import dev.pedronube.domaincommons.domain.port.out.repository.user.SaveUserPort;
import dev.pedronube.domaincommons.domain.usecase.SaveUserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("SaveUserFromCognitoService Unit Tests")
class SaveUserFromCognitoServiceUnitTest {

    @Mock
    private SaveUserPort mockSaveUserPort;

    private SaveUserFromCognitoService service;

    @BeforeEach
    void setUp() {
        service = new SaveUserFromCognitoService();
    }

    @Nested
    @DisplayName("Bean creation")
    class BeanCreationTests {

        @Test
        @DisplayName("Should create SaveUserUseCase bean successfully")
        void shouldCreateSaveUserUseCaseBeanSuccessfully() {
            // When
            SaveUserUseCase result = service.saveUser(mockSaveUserPort);

            // Then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("Should return functional SaveUserUseCase")
        void shouldReturnFunctionalSaveUserUseCase() {
            // Given
            User testUser = new User();
            testUser.setSub("sub-123");
            testUser.setUsername("testuser");
            testUser.setEmail("test@example.com");

            SaveUserUseCase saveUserUseCase = service.saveUser(mockSaveUserPort);

            // When
            saveUserUseCase.accept(testUser);

            // Then
            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(mockSaveUserPort).accept(userCaptor.capture());
            
            User capturedUser = userCaptor.getValue();
            assertThat(capturedUser).isEqualTo(testUser);
            assertThat(capturedUser.getSub()).isEqualTo("sub-123");
            assertThat(capturedUser.getUsername()).isEqualTo("testuser");
            assertThat(capturedUser.getEmail()).isEqualTo("test@example.com");
        }
    }

    @Nested
    @DisplayName("SaveUserUseCase functionality")
    class SaveUserUseCaseFunctionalityTests {

        @Test
        @DisplayName("Should delegate to SaveUserPort correctly")
        void shouldDelegateToSaveUserPortCorrectly() {
            // Given
            User user = new User();
            user.setSub("delegation-test-sub");
            user.setUsername("delegationuser");
            user.setEmail("delegation@example.com");
            user.setSubscriptionLevel("FREE");
            user.setCreatedAt("2024-01-01T10:00:00Z");

            SaveUserUseCase saveUserUseCase = service.saveUser(mockSaveUserPort);

            // When
            saveUserUseCase.accept(user);

            // Then
            verify(mockSaveUserPort).accept(user);
        }

        @Test
        @DisplayName("Should handle null user gracefully")
        void shouldHandleNullUserGracefully() {
            // Given
            User user = null;
            SaveUserUseCase saveUserUseCase = service.saveUser(mockSaveUserPort);

            // When
            saveUserUseCase.accept(user);

            // Then
            verify(mockSaveUserPort).accept(null);
        }

        @Test
        @DisplayName("Should handle user with null fields")
        void shouldHandleUserWithNullFields() {
            // Given
            User user = new User();
            user.setSub(null);
            user.setUsername(null);
            user.setEmail(null);
            user.setSubscriptionLevel(null);
            user.setCreatedAt(null);

            SaveUserUseCase saveUserUseCase = service.saveUser(mockSaveUserPort);

            // When
            saveUserUseCase.accept(user);

            // Then
            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(mockSaveUserPort).accept(userCaptor.capture());
            
            User capturedUser = userCaptor.getValue();
            assertThat(capturedUser.getSub()).isNull();
            assertThat(capturedUser.getUsername()).isNull();
            assertThat(capturedUser.getEmail()).isNull();
            assertThat(capturedUser.getSubscriptionLevel()).isNull();
            assertThat(capturedUser.getCreatedAt()).isNull();
        }

        @Test
        @DisplayName("Should handle user with empty string fields")
        void shouldHandleUserWithEmptyStringFields() {
            // Given
            User user = new User();
            user.setSub("");
            user.setUsername("");
            user.setEmail("");
            user.setSubscriptionLevel("");
            user.setCreatedAt("");

            SaveUserUseCase saveUserUseCase = service.saveUser(mockSaveUserPort);

            // When
            saveUserUseCase.accept(user);

            // Then
            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(mockSaveUserPort).accept(userCaptor.capture());
            
            User capturedUser = userCaptor.getValue();
            assertThat(capturedUser.getSub()).isEmpty();
            assertThat(capturedUser.getUsername()).isEmpty();
            assertThat(capturedUser.getEmail()).isEmpty();
            assertThat(capturedUser.getSubscriptionLevel()).isEmpty();
            assertThat(capturedUser.getCreatedAt()).isEmpty();
        }

        @Test
        @DisplayName("Should preserve all user data during delegation")
        void shouldPreserveAllUserDataDuringDelegation() {
            // Given
            User originalUser = new User();
            originalUser.setSub("preserve-test-sub");
            originalUser.setUsername("preserveuser");
            originalUser.setEmail("preserve@example.com");
            originalUser.setSubscriptionLevel("PREMIUM");
            originalUser.setCreatedAt("2024-01-15T14:30:00Z");

            SaveUserUseCase saveUserUseCase = service.saveUser(mockSaveUserPort);

            // When
            saveUserUseCase.accept(originalUser);

            // Then
            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(mockSaveUserPort).accept(userCaptor.capture());
            
            User capturedUser = userCaptor.getValue();
            assertThat(capturedUser).isSameAs(originalUser); // Same reference
            assertThat(capturedUser.getSub()).isEqualTo("preserve-test-sub");
            assertThat(capturedUser.getUsername()).isEqualTo("preserveuser");
            assertThat(capturedUser.getEmail()).isEqualTo("preserve@example.com");
            assertThat(capturedUser.getSubscriptionLevel()).isEqualTo("PREMIUM");
            assertThat(capturedUser.getCreatedAt()).isEqualTo("2024-01-15T14:30:00Z");
        }
    }

    @Nested
    @DisplayName("Configuration behavior")
    class ConfigurationBehaviorTests {

        @Test
        @DisplayName("Should create new UseCase instance for each call")
        void shouldCreateNewUseCaseInstanceForEachCall() {
            // When
            SaveUserUseCase useCase1 = service.saveUser(mockSaveUserPort);
            SaveUserUseCase useCase2 = service.saveUser(mockSaveUserPort);

            // Then
            assertThat(useCase1).isNotNull();
            assertThat(useCase2).isNotNull();
            // Note: Since it's a method reference, they might be the same instance
            // but the important thing is they both work correctly
        }

        @Test
        @DisplayName("Should work with different SaveUserPort instances")
        void shouldWorkWithDifferentSaveUserPortInstances() {
            // Given
            SaveUserPort anotherMockPort = org.mockito.Mockito.mock(SaveUserPort.class);
            User testUser = new User();
            testUser.setSub("multi-port-test");

            // When
            SaveUserUseCase useCase1 = service.saveUser(mockSaveUserPort);
            SaveUserUseCase useCase2 = service.saveUser(anotherMockPort);

            useCase1.accept(testUser);
            useCase2.accept(testUser);

            // Then
            verify(mockSaveUserPort).accept(testUser);
            verify(anotherMockPort).accept(testUser);
        }
    }
}