# Acceptance Testing

Tests de aceptación para validar la funcionalidad completa de la aplicación serverless desplegada en AWS.

## Estructura

```
acceptance-testing/
├── by-service/                 # Tests organizados por servicio
│   └── authentication-service/
│       └── login-successful.http
├── http-client.env.json        # Configuración de entornos
└── README.md                   # Este archivo
```

## Configuración de Entornos

### http-client.env.json

Define las URLs y configuraciones para cada entorno:

```json
{
  "local": {
    "host": "http://localhost:8080",
    "stageName": "local"
  },
  "development": {
    "host": "https://tu-api-gateway-url.execute-api.eu-west-1.amazonaws.com",
    "stageName": "dev"
  },
  "preproduction": {
    "host": "https://api.tu-dominio.com",
    "stageName": "pre"
  },
  "production": {
    "host": "https://api.tu-dominio.com",
    "stageName": "pro"
  }
}
```

## Tests por Servicio

### Authentication Service

**login-successful.http:**
- Autentica usuario válido
- Captura JWT token en variable global
- Valida respuesta exitosa
- Prepara token para tests posteriores

```http
POST {{host}}/{{stageName}}/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "pwdtestuser"
}

> {%
    const token = response.body.jwtToken;
    client.test("Token JWT capturado", function() {
        client.assert(token !== null && token.length > 0);
    });
    client.global.set("jwt_token", token);
%}
```

## Uso con IntelliJ IDEA

### Ejecutar Tests

1. **Seleccionar entorno**: Cambiar environment en HTTP Client
2. **Ejecutar request**: Click en ▶️ junto al request
3. **Ver resultados**: Panel de resultados muestra response y tests
4. **Variables globales**: Disponibles para requests posteriores

### Variables Disponibles

- `{{host}}` - URL base del API
- `{{stageName}}` - Nombre del stage/entorno
- `{{jwt_token}}` - Token JWT capturado (global)

## Flujo de Testing

### 1. Autenticación
```http
POST {{host}}/{{stageName}}/login
# → Captura jwt_token
```

### 2. Operaciones Protegidas
```http
GET {{host}}/{{stageName}}/protected-endpoint
Authorization: Bearer {{jwt_token}}
```

## Validaciones Automáticas

Los tests incluyen validaciones JavaScript:

```javascript
// Validar status code
client.test("Status 200", function() {
    client.assert(response.status === 200);
});

// Validar estructura de respuesta
client.test("JWT presente", function() {
    client.assert(response.body.jwtToken !== null);
});

// Capturar datos para tests posteriores
client.global.set("variable_name", response.body.field);
```

## Entornos de Testing

### Local (desarrollo)
- **Host**: `localhost:8080`
- **Uso**: Testing durante desarrollo
- **Requisitos**: Aplicación ejecutándose localmente

### Development (AWS)
- **Host**: API Gateway desplegado
- **Uso**: Testing de integración
- **Requisitos**: Infraestructura desplegada con Terraform

### Preproduction/Production
- **Host**: Dominio personalizado
- **Uso**: Testing de aceptación
- **Requisitos**: DNS y certificados configurados

## Mejores Prácticas

### Organización
- ✅ **Por servicio**: Carpetas separadas por módulo
- ✅ **Nombres descriptivos**: `login-successful.http`, `user-create-valid.http`
- ✅ **Variables globales**: Reutilizar tokens y datos

### Testing
- ✅ **Validaciones automáticas**: JavaScript post-request
- ✅ **Datos de prueba**: Usuarios y datos específicos para testing
- ✅ **Cleanup**: Limpiar datos creados durante tests

### Mantenimiento
- ✅ **Actualizar URLs**: Mantener http-client.env.json actualizado
- ✅ **Documentar cambios**: Actualizar README con nuevos tests
- ✅ **Versionado**: Incluir tests en control de versiones