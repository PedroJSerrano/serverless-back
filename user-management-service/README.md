# User Management Service

Plataforma de gestión de usuarios que proporciona operaciones CRUD para la administración de usuarios.

## Funcionalidades

- **Registro de usuarios** - Creación de nuevos usuarios
- **Actualización de usuarios** - Modificación de datos de usuario
- **Eliminación de usuarios** - Borrado de usuarios del sistema

## Arquitectura

```
├── domain/
│   ├── User.java                 # Entidad de usuario
│   └── exceptions/
│       ├── DuplicateUserException.java
│       └── UserNotFoundException.java
├── application/
│   ├── port/in/
│   │   ├── RegisterUserUseCase.java    # Caso de uso de registro
│   │   ├── UpdateUserUseCase.java      # Caso de uso de actualización
│   │   └── DeleteUserUseCase.java      # Caso de uso de eliminación
│   └── service/
│       ├── RegisterUserService.java    # Implementación de registro
│       ├── UpdateUserService.java      # Implementación de actualización
│       └── DeleteUserService.java      # Implementación de eliminación
└── infrastructure/
    └── adapter/
        └── in/web/
            ├── UserApiFunctions.java   # Funciones Lambda
            └── dto/                    # DTOs de entrada/salida
                ├── UserRegistrationRequest.java
                └── UserRegistrationResponse.java
```

## Estado del Desarrollo

⚠️ **En desarrollo** - Este módulo está actualmente en construcción. Las funciones Lambda y endpoints están pendientes de implementación.

## Despliegue

```bash
# Construir el módulo
mvn clean package

# Desplegar con Terraform (desde raíz del proyecto)
cd ../terraform
terraform apply
```

## Testing

```bash
# Ejecutar tests
mvn test

# Generar reporte de cobertura
mvn verify
```