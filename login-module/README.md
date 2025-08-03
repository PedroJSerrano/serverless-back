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

```
├── domain/
│   ├── UserCredentials.java      # Entidad de credenciales
│   ├── UserPrincipal.java        # Entidad de usuario autenticado
│   ├── UserSession.java          # Entidad de sesión
│   └── exceptions/
│       └── InvalidCredentialsException.java
├── application/
│   ├── port/in/
│   │   └── LoginUserUseCase.java # Caso de uso de login
│   └── service/
│       └── LoginUserService.java # Implementación del caso de uso
├── infrastructure/
│   └── adapter/
│       └── in/web/
│           ├── LoginApiFunctions.java # Función Lambda
│           └── dto/               # DTOs de entrada/salida
└── config/
    └── DynamoDbConfig.java       # Configuración de DynamoDB
```

## Recursos AWS

- **Lambda Function:** `LoginFunction`
- **DynamoDB Table:** `users`
- **SSM Parameter:** `/login/jwt/secret`
- **API Gateway:** Endpoint HTTP POST `/login`

## Variables de Entorno

- `JWT_EXPIRATION`: Tiempo de expiración del token (default: 3600000ms)
- `MY_AWS_REGION`: Región AWS (default: eu-west-1)

## Despliegue

```bash
# Construir el módulo
mvn clean package

# Desplegar
sam build
sam deploy --guided
```

## Testing

```bash
# Ejecutar tests
mvn test

# Generar reporte de cobertura
mvn verify
```

## Desarrollo Local

```bash
# Ejecutar función localmente
sam local invoke LoginFunction --event ../events/event.json

# Ejecutar API localmente
sam local start-api
curl -X POST http://localhost:3000/login \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"test"}'
```