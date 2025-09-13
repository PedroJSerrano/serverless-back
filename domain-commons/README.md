# Domain Commons

Plataforma de dominio compartido que proporciona elementos comunes reutilizables entre servicios siguiendo principios DDD y arquitectura hexagonal.

## Funcionalidades

- **DTOs compartidos** - Objetos de transferencia de datos comunes
- **Puertos reutilizables** - Interfaces de entrada y salida compartidas
- **Casos de uso comunes** - Lógica de negocio reutilizable
- **Modelos de dominio** - Entidades y value objects compartidos
- **Validaciones de negocio** - Reglas de validación comunes

## Estructura

```
├── domain/
│   ├── model/              # Modelos de dominio compartidos
│   ├── exceptions/         # Excepciones de negocio comunes
│   └── validation/         # Validaciones de negocio
├── port/
│   ├── dto/               # DTOs compartidos entre servicios
│   ├── in/                # Puertos de entrada comunes
│   └── out/               # Puertos de salida comunes
└── usecase/
    └── common/            # Casos de uso reutilizables
```

## Uso en otros servicios

### Dependencia Maven

```xml
<dependency>
    <groupId>dev.pedroenlanube</groupId>
    <artifactId>business-platform-commons</artifactId>
    <version>${pedronube.business.platform.common.version}</version>
</dependency>
```

### Importación de componentes

```java
// Importar DTOs compartidos
import dev.pedroenlanube.domaincommons.port.dto.CommonResponse;

// Importar puertos compartidos
import dev.pedroenlanube.domaincommons.port.in.CommonUseCase;

// Importar modelos de dominio
import dev.pedroenlanube.domaincommons.domain.model.BaseEntity;
```

## Principios de Diseño

### DRY (Don't Repeat Yourself)
- Elementos comunes centralizados
- Evita duplicación entre servicios
- Facilita mantenimiento

### Arquitectura Hexagonal
- Puertos y adaptadores bien definidos
- Separación clara de responsabilidades
- Independencia de frameworks

### Cloud Agnostic
- Sin dependencias específicas de AWS
- Portabilidad entre proveedores cloud
- Abstracción de infraestructura

## Estado del Desarrollo

🚧 **En desarrollo** - Este módulo está en construcción inicial.

**Elementos planificados:**
- DTOs de respuesta estándar
- Excepciones de negocio comunes
- Validaciones de entrada
- Puertos de auditoría
- Casos de uso de paginación

## Testing

```bash
# Ejecutar tests unitarios
mvn test

# Generar reporte de cobertura
mvn verify
```

## Tecnologías

- **Spring Boot 3.5.4** - Framework base
- **Validation API** - Validaciones estándar
- **JUnit 5 + Mockito** - Testing framework
- **Lombok** - Reducción de boilerplate