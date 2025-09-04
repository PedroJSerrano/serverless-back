# AWS Common Infrastructure

Módulo de utilidades compartidas para la integración con servicios AWS. Proporciona configuraciones y beans comunes reutilizables por todos los módulos de la aplicación.

## Funcionalidades

- **Configuración AWS SDK** - Clients optimizados para Lambda
- **Configuración DynamoDB** - Enhanced client con configuración específica
- **Utilidades comunes** - Beans y configuraciones compartidas
- **Testing utilities** - Configuraciones para tests unitarios

## Componentes

### Configuraciones AWS

```
├── config/
│   ├── AwsConfig.java           # Configuración de AWS SDK clients
│   └── DynamoDbConfig.java      # Configuración de DynamoDB Enhanced Client
```

**AwsConfig.java:**
- DynamoDB client optimizado para Lambda
- Configuración de región automática
- Timeouts y retry policies optimizados

**DynamoDbConfig.java:**
- DynamoDB Enhanced Client
- Configuración de mappers
- Optimizaciones para cold start

### Testing

```
├── test/config/
│   ├── AwsConfigUnitTest.java
│   ├── DynamoDbConfigUnitTest.java
│   └── TestDynamoDbConfig.java  # Configuración para tests
```

## Uso en otros módulos

### Dependencia Maven

```xml
<dependency>
    <groupId>dev.pedroenlanube</groupId>
    <artifactId>aws-common-infrastructure</artifactId>
    <version>${pedronube.awscommon.infrastructure.version}</version>
</dependency>
```

### ComponentScan

```java
@ComponentScan(basePackages = {
    "dev.pedronube.{tu-modulo}", 
    "dev.pedronube.awscommon"
})
```

## Arquitectura

Sigue los principios de **arquitectura hexagonal**:
- **Sin lógica de negocio** - Solo configuraciones técnicas
- **Reutilizable** - Compartido entre todos los módulos
- **Testeable** - Configuraciones mockeables para tests

## Testing

```bash
# Ejecutar tests unitarios
mvn test

# Generar reporte de cobertura
mvn verify
```

### Cobertura de Tests
- **Cobertura de líneas**: >90%
- **Cobertura de métodos**: >90%
- **Tests unitarios**: Verifican configuraciones con mocks

## Tecnologías

- **Spring Boot 3.5.4** - Framework de configuración
- **AWS SDK v2** - Clients nativos de AWS
- **DynamoDB Enhanced Client** - ORM para DynamoDB
- **Spring Cloud AWS 3.2.1** - Integración Spring-AWS
- **JUnit 5 + Mockito** - Testing framework

## Optimizaciones Lambda

Las configuraciones están optimizadas para AWS Lambda:
- **Cold start reduction** - Clients lazy-initialized
- **Memory optimization** - Configuraciones mínimas
- **Timeout configuration** - Timeouts apropiados para Lambda
- **Region detection** - Automática desde environment