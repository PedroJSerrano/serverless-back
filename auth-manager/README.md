# Auth Manager

Módulo de autenticación de usuarios que proporciona funcionalidad de login con JWT.

## Funcionalidades

- **Autenticación de usuarios** - Validación de credenciales contra DynamoDB
- **Generación de JWT** - Tokens seguros con expiración configurable
- **Gestión de secretos** - Integración con AWS Systems Manager
- 🚧 **Lambda Authorizer** - Autorización de recursos privados (en construcción)

## API Endpoints

### POST /login
Autentica un usuario y devuelve un token JWT.

**Request:**
```json
{
  "username": "usuario",
  "password": "contraseña"
}
```

**Response:**
```json
{
  "jwtToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

## Arquitectura

Implementa **arquitectura hexagonal** con separación clara de responsabilidades:

```
├── domain/
│   ├── model/
│   │   └── User.java             # Entidad de usuario
│   ├── exceptions/
│   │   └── InvalidCredentialsException.java
│   └── port/
│       ├── dto/
│       │   ├── ValidateUserCommand.java
│       │   └── ValidateUserResponse.java
│       ├── in/
│       │   └── LoginUserUseCase.java # Puerto de entrada - Caso de uso
│       └── out/
│           ├── UserRepositoryPort.java    # Puerto de salida - Repositorio
│           ├── TokenServicePort.java      # Puerto de salida - Tokens
│           └── JwtSecretProviderPort.java # Puerto de salida - Secretos
├── application/
│   └── service/
│       └── LoginUserService.java # Implementación del caso de uso
├── infrastructure/
│   └── adapter/
│       ├── in/web/
│       │   ├── dto/
│       │   │   ├── LoginRequest.java
│       │   │   └── LoginResponse.java
│       │   └── LoginApiFunctions.java # Adaptador web - Función Lambda
│       └── out/
│           ├── persistence/
│           │   ├── DynamoDbUserRepositoryAdapter.java
│           │   └── model/
│           │       └── UserDynamoEntity.java
│           └── security/
│               ├── SecurityAdapterConfig.java
│               └── SsmJwtSecretProviderAdapter.java
├── config/
│   ├── AwsConfig.java           # Configuración de AWS clients
│   └── DynamoDbConfig.java      # Configuración de DynamoDB
└── AuthManagerApplication.java  # Clase principal
```

## Recursos AWS

- **Lambda Function:** `LoginFunction`
- 🚧 **Lambda Authorizer:** `AuthorizerFunction` (en construcción)
- **DynamoDB Table:** `users`
- **SSM Parameter:** `/login/jwt/secret`
- **API Gateway:** Endpoint HTTP POST `/login`

## Variables de Entorno

- `JWT_EXPIRATION`: Tiempo de expiración del token (default: 3600000ms)
- `SPRING_CLOUD_AWS_REGION_STATIC`: Región AWS (configurada automáticamente)
- `SPRING_CLOUD_FUNCTION_DEFINITION`: Función a ejecutar (loginFunction)

## Despliegue

```bash
# Construir el módulo
mvn clean package

# Desplegar con Terraform (desde raíz del proyecto)
cd ../terraform
terraform apply
```

## Testing

El módulo cuenta con **cobertura completa de tests** (>90% en todas las métricas):

### Estructura de Tests
- **Tests Unitarios**: Verifican lógica de negocio con mocks

### Clases de Test por Componente
```
├── config/
│   └── AwsConfigUnitTest.java
├── application/service/
│   └── LoginUserServiceUnitTest.java
└── infrastructure/adapter/
    ├── in/web/
    │   └── LoginApiFunctionsUnitTest.java
    └── out/
        ├── persistence/
        │   └── DynamoDbUserRepositoryAdapterUnitTest.java
        └── security/
            ├── SecurityAdapterConfigUnitTest.java
            └── SsmJwtSecretProviderAdapterUnitTest.java
```

### Comandos de Testing
```bash
# Ejecutar todos los tests
mvn test

# Generar reporte de cobertura
mvn verify

# Ejecutar solo tests unitarios
mvn test -Dtest="**/*UnitTest"



```

## Desarrollo Local

### Prerrequisitos
- **Java 21**
- **Maven 3.8+**
- **Terraform** (para despliegue)
- **AWS CLI** configurado
- **Credenciales AWS** (para despliegue)

### Comandos de Desarrollo
```bash
# Construir el módulo
mvn clean compile

# Ejecutar todos los tests
mvn test

# Testing E2E con HTTP Client
# Usar archivos .http en ../e2e-tests/by-service/auth-manager/
# Configurar entorno en ../e2e-tests/http-client.env.json
```

## Tecnologías Utilizadas

- **Java 21** con Spring Cloud Function
- **Spring Cloud AWS** para integración con servicios AWS
- **Maven** para gestión de dependencias
- **JUnit 5** + **Mockito** para testing
- **Mocks** para tests de integración (sin dependencias externas)
- **JaCoCo** para cobertura de código
- **AWS Lambda** para ejecución serverless
- **DynamoDB** para persistencia
- **AWS Systems Manager** para gestión de secretos
- **JWT** para autenticación