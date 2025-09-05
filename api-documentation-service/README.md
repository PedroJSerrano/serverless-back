# API Documentation Service

Servicio de documentación unificada que agrega los fragmentos OpenAPI de todos los servicios y genera documentación interactiva profesional.

## Funcionalidades

- **Agregación OpenAPI** - Fusiona fragmentos de múltiples servicios
- **Generación Redocly** - Documentación HTML profesional
- **Despliegue automático** - S3 + CloudFront para distribución global
- **CI/CD Integration** - Actualización automática con deployments

## Arquitectura

```
├── src/main/java/
│   └── dev/pedroenlanube/apidocs/
│       ├── config/         # Configuración OpenAPI
│       └── aggregator/     # Lógica de agregación
├── target/
│   ├── api-fragments/      # Fragmentos de cada servicio
│   │   ├── authentication-service.json
│   │   └── user-management-service.json
│   ├── openapi-final.json  # Documentación unificada
│   └── docs.html          # HTML final generado
└── pom.xml
```

## Flujo de Generación

### 1. Recolección de Fragmentos
```bash
# Cada servicio genera su fragmento OpenAPI
mvn compile # → target/api-fragments/{service}.json
```

### 2. Agregación con Redocly
```bash
# Fusiona todos los fragmentos en un único archivo
redocly bundle target/api-fragments/*.json -o target/openapi-final.json
```

### 3. Generación de Documentación
```bash
# Construye la página HTML final
redocly build-docs target/openapi-final.json -o target/docs.html
```

### 4. Despliegue (Futuro)
```bash
# Subida automática a S3 + invalidación CloudFront
aws s3 sync target/ s3://docs-bucket/
aws cloudfront create-invalidation --distribution-id XXXXX --paths "/*"
```

## Configuración por Servicio

Cada servicio debe generar su fragmento OpenAPI:

### Authentication Service
```java
@Bean
public OpenAPI authenticationServiceOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Authentication Service API")
            .version("1.4.0")
            .description("Identity platform endpoints"))
        .servers(List.of(
            new Server().url("https://api.pedroenlanube.dev").description("Production")
        ));
}
```

### User Management Service
```java
@Bean
public OpenAPI userManagementServiceOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("User Management Service API")
            .version("1.0.0")
            .description("User platform endpoints"));
}
```

## Resultado Final

### Documentación Unificada
🔗 **[API Documentation](https://docs.pedroenlanube.dev)** *(próximamente)*

**Características:**
- **UI Profesional** - Redocly theme customizado
- **Navegación por servicios** - Organizada por módulos
- **Testing interactivo** - Try-it-out integrado
- **Esquemas detallados** - Request/Response completos
- **Ejemplos reales** - Datos de ejemplo por endpoint

### Infraestructura de Hosting
- **S3 Bucket** - Hosting estático económico
- **CloudFront** - CDN global con HTTPS
- **Custom Domain** - docs.pedroenlanube.dev
- **Actualización automática** - CI/CD integration

## Estado del Desarrollo

🚧 **En desarrollo** - Implementación en progreso.

**Tareas pendientes:**
- Configuración OpenAPI beans por servicio
- Script de agregación Maven
- Integración Redocly
- Configuración S3 + CloudFront
- Pipeline CI/CD

## Comandos de Desarrollo

```bash
# Generar fragmentos (desde cada servicio)
mvn compile

# Agregar documentación (desde api-documentation-service)
mvn package

# Verificar resultado
open target/docs.html
```

## Tecnologías

- **OpenAPI 3.0** - Especificación estándar de APIs
- **Redocly CLI** - Generación de documentación
- **Maven** - Build y agregación
- **S3 + CloudFront** - Hosting y distribución
- **Spring Boot** - Framework base