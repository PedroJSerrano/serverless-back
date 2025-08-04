# Login Module

Módulo de autenticación de usuarios que proporciona funcionalidad de login con JWT.

## Funcionalidades

- **Autenticación de usuarios** - Validación de credenciales contra DynamoDB
- **Generación de JWT** - Tokens seguros con expiración configurable
- **Gestión de secretos** - Integración con AWS Systems Manager

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
│   ├── UserCredentials.java      # Entidad de credenciales
│   ├── UserPrincipal.java        # Entidad de usuario autenticado
│   ├── UserSession.java          # Entidad de sesión
│   └── exceptions/
│       └── InvalidCredentialsException.java
├── application/
│   ├── port/in/
│   │   └── LoginUserUseCase.java # Puerto de entrada - Caso de uso
│   ├── port/out/
│   │   ├── UserRepositoryPort.java    # Puerto de salida - Repositorio
│   │   ├── TokenServicePort.java      # Puerto de salida - Tokens
│   │   └── JwtSecretProviderPort.java # Puerto de salida - Secretos
│   └── service/
│       └── LoginUserService.java # Implementación del caso de uso
├── infrastructure/
│   └── adapter/
│       ├── in/web/
│       │   ├── LoginApiFunctions.java # Adaptador web - Función Lambda
│       │   └── dto/               # DTOs de entrada/salida
│       └── out/
│           ├── persistence/
│           │   └── DynamoDbUserRepositoryAdapter.java
│           └── security/
│               ├── SecurityAdapterConfig.java
│               └── SsmJwtSecretProviderAdapter.java
└── config/
    ├── DynamoDbConfig.java       # Configuración de DynamoDB
    └── AwsConfig.java           # Configuración de AWS clients
```

## Recursos AWS

- **Lambda Function:** `LoginFunction`
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

# Desplegar
sam build
sam deploy --guided
```

## Testing

El módulo cuenta con **cobertura completa de tests** (>90% en todas las métricas):

### Estructura de Tests
- **Tests Unitarios**: Verifican lógica de negocio con mocks
- **Tests de Integración**: Verifican configuración Spring con LocalStack
- **Tests de Producción**: Verifican comportamiento con configuración AWS real

### Clases de Test por Componente
```
├── config/
│   ├── AwsConfigUnitTest.java
│   ├── AwsConfigIntegrationTest.java
│   ├── AwsConfigProductionTest.java
│   ├── DynamoDbConfigTest.java
│   └── DynamoDbConfigIntegrationTest.java
├── application/service/
│   ├── LoginUserServiceUnitTest.java
│   └── LoginUserServiceIntegrationTest.java
├── infrastructure/adapter/
│   ├── in/web/
│   │   ├── LoginApiFunctionsUnitTest.java
│   │   └── LoginApiFunctionsIntegrationTest.java
│   └── out/security/
│       ├── SecurityAdapterConfigUnitTest.java
│       ├── SecurityAdapterConfigIntegrationTest.java
│       ├── SsmJwtSecretProviderAdapterUnitTest.java
│       └── SsmJwtSecretProviderAdapterIntegrationTest.java
```

### Comandos de Testing
```bash
# Ejecutar todos los tests
mvn test

# Generar reporte de cobertura
mvn verify

# Ejecutar solo tests unitarios
mvn test -Dtest="**/*UnitTest"

# Ejecutar solo tests de integración
mvn test -Dtest="**/*IntegrationTest"
```

## Desarrollo Local

### Prerrequisitos
- **Java 21**
- **Maven 3.8+**
- **SAM CLI**
- **Docker** (para LocalStack y tests de integración)

### Comandos de Desarrollo
```bash
# Construir el módulo
mvn clean compile

# Ejecutar tests con LocalStack
mvn test

# Ejecutar función localmente
sam local invoke LoginFunction --event ../events/event-login-successful.json

# Ejecutar API localmente
sam local start-api
curl -X POST http://localhost:3000/login \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"test"}'
```

## Tecnologías Utilizadas

- **Java 21** con Spring Cloud Function
- **Spring Cloud AWS** para integración con servicios AWS
- **Maven** para gestión de dependencias
- **JUnit 5** + **Mockito** para testing
- **TestContainers** + **LocalStack** para tests de integración
- **JaCoCo** para cobertura de código
- **AWS Lambda** para ejecución serverless
- **DynamoDB** para persistencia
- **AWS Systems Manager** para gestión de secretos
- **JWT** para autenticación