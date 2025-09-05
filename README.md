# serverless-back

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring](https://img.shields.io/badge/Spring%20Cloud%20Function-4.3.0-green)
![Terraform](https://img.shields.io/badge/Terraform-IaC-purple)
![AWS](https://img.shields.io/badge/AWS-Lambda%20%7C%20DynamoDB%20%7C%20API%20Gateway-yellow)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=pedroenlanube_serverless-back&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=pedroenlanube_serverless-back)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=pedroenlanube_serverless-back&metric=coverage)](https://sonarcloud.io/summary/new_code?id=pedroenlanube_serverless-back)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=pedroenlanube_serverless-back&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=pedroenlanube_serverless-back)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=pedroenlanube_serverless-back&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=pedroenlanube_serverless-back)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=pedroenlanube_serverless-back&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=pedroenlanube_serverless-back)

Aplicación serverless modular desarrollada con **Java 21** y **arquitectura hexagonal**. Cada módulo se puede desplegar independientemente usando Terraform.

## Stack Tecnológico

- **Java 21** con Spring Cloud Function 4.3.0
- **Spring Boot 3.5.4** - Framework base
- **Maven** para gestión de dependencias (multi-módulo)
- **AWS Lambda** para funciones serverless
- **DynamoDB** para persistencia NoSQL
- **API Gateway** para endpoints HTTP
- **AWS Systems Manager (SSM)** para gestión de secretos
- **Spring Cloud AWS 3.2.1** para integración con servicios AWS
- **JWT (jsonwebtoken 0.12.6)** para autenticación
- **Lombok 1.18.30** para reducción de boilerplate
- **JaCoCo 0.8.12** para cobertura de tests (>90%)
- **JUnit 5 + Mockito** para testing
- **Terraform** para Infrastructure as Code (IaC)

## Módulos

### 🔐 [Authentication Service](./authentication-service/README.md) ✅ **Completo**
Plataforma de identidad que proporciona:
- Validación de credenciales contra DynamoDB
- Generación de tokens JWT con expiración configurable
- Integración con AWS Systems Manager para secretos
- API REST: `POST /login`
- 🚧 **Lambda Authorizer** para recursos privados (en construcción)

**Tecnologías:** Spring Cloud Function, DynamoDB, SSM, JWT

### 👥 [User Management Service](./user-management-service/README.md) 🚧 **En desarrollo**
Plataforma de gestión de usuarios que incluirá:
- Registro de nuevos usuarios (CRUD)
- Actualización de datos de usuario
- Eliminación de usuarios
- Validaciones de negocio

**Tecnologías:** Spring Cloud Function, DynamoDB, JWT

### 🌐 [Cloud Infrastructure Commons](./cloud-infrastructure-commons/README.md) ✅ **Completo**
Plataforma común para integración con servicios cloud:
- Configuraciones AWS SDK optimizadas para Lambda
- DynamoDB Enhanced Client
- Beans y configuraciones reutilizables
- Testing utilities

**Tecnologías:** Spring Boot, AWS SDK v2, DynamoDB Enhanced Client

### 🏢 [Domain Commons](./domain-commons/README.md) 🚧 **En desarrollo**
Plataforma de dominio compartido:
- DTOs comunes entre servicios
- Puertos y casos de uso reutilizables
- Modelos de dominio compartidos
- Validaciones de negocio comunes

**Tecnologías:** Spring Boot, Validation API

### 📝 [API Documentation Service](./api-documentation-service/README.md) 🚧 **En desarrollo**
Servicio de documentación unificada:
- Agregación de OpenAPI fragments
- Generación de documentación con Redocly
- Despliegue automático en S3+CloudFront
- UI interactiva profesional

**Tecnologías:** OpenAPI 3.0, Redocly, Maven

### 🏗️ [Terraform](./terraform/README.md) ✅ **Completo**
Infraestructura como código para despliegue en AWS:
- Módulos reutilizables para Lambda y recursos compartidos
- Backend remoto en S3 para state management
- Configuración por servicio
- Tags automáticos para organización

**Tecnologías:** Terraform, AWS Provider

### ✅ [Acceptance Testing](./acceptance-testing/README.md) ✅ **Completo**
Tests de aceptación con IntelliJ HTTP Client:
- Tests organizados por servicio
- Configuración multi-entorno
- Captura automática de tokens JWT
- Validaciones JavaScript
- Flujos completos de usuario

**Tecnologías:** IntelliJ HTTP Client, JavaScript

## Arquitectura

### Diagrama de Arquitectura General

```mermaid
graph TB
    subgraph "AWS Cloud"
        subgraph "API Layer"
            AG[API Gateway HTTP v2]
        end
        
        subgraph "Compute Layer"
            AL[Authentication Service Lambda]
            UL[User Management Service Lambda]
        end
        
        subgraph "Data Layer"
            DB[(DynamoDB Users Table)]
            SSM[Systems Manager]
        end
        
        subgraph "Monitoring"
            CW[CloudWatch Logs]
            AR[AppRegistry]
        end
    end
    
    subgraph "Client"
        HTTP[HTTP Client]
        E2E[E2E Tests]
    end
    
    HTTP --> AG
    E2E --> AG
    AG --> AL
    AG --> UL
    AL --> DB
    AL --> SSM
    UL --> DB
    AL --> CW
    UL --> CW
    AL --> AR
    UL --> AR
```

### Arquitectura Hexagonal por Módulo

```mermaid
graph LR
    subgraph "Infrastructure Layer"
        WEB[Web Adapter<br/>Lambda Function]
        DB[DynamoDB Adapter]
        SSM[SSM Adapter]
    end
    
    subgraph "Application Layer"
        UC[Use Case Service]
    end
    
    subgraph "Domain Layer"
        PORT_IN[Input Port]
        PORT_OUT[Output Ports]
        DOMAIN[Domain Models]
        RULES[Business Rules]
    end
    
    WEB --> PORT_IN
    PORT_IN --> UC
    UC --> PORT_OUT
    PORT_OUT --> DB
    PORT_OUT --> SSM
    UC --> DOMAIN
    UC --> RULES
```

Cada módulo implementa **arquitectura hexagonal**:
- **Domain** - Clases y reglas de negocio, sin dependencias externas. Incluye la definición de los puertos y los casos de uso
- **Application** - Servicios que definen los beans de los casos de uso
- **Infrastructure** - Adaptadores (web, persistencia, externos)

## 📚 Documentación de la API

La documentación interactiva de la API está disponible en:

🔗 **[API Documentation](https://docs.pedroenlanube.dev)** *(próximamente)*

**Características:**
- 📖 **Documentación interactiva** - Redocly UI profesional
- 🔍 **Endpoints por módulo** - Organizados por servicio
- 🧪 **Testing integrado** - Probar endpoints directamente
- 📋 **Esquemas detallados** - Request/Response con ejemplos
- 🔄 **Actualización automática** - Sincronizada con deployments

**Tecnologías:**
- **OpenAPI 3.0** - Especificación estándar
- **Redocly** - Generación de documentación
- **S3 + CloudFront** - Hosting y distribución global
- **CI/CD Integration** - Actualización automática

## Herramientas de Desarrollo

**IDEs recomendados:**
* [IntelliJ IDEA](https://docs.aws.amazon.com/toolkit-for-jetbrains/latest/userguide/welcome.html) (recomendado) - Con AWS Toolkit
* [VS Code](https://docs.aws.amazon.com/toolkit-for-vscode/latest/userguide/welcome.html) - Con AWS Toolkit

**Herramientas de infraestructura:**
* **Terraform** - Para gestión de infraestructura AWS
* **AWS CLI** - Para interacción con servicios AWS
* **HTTP Client** (IntelliJ) - Para testing E2E de APIs

## Despliegue de la Aplicación

La aplicación utiliza **Terraform** para Infrastructure as Code (IaC), permitiendo despliegues reproducibles y versionado de infraestructura.

### Prerrequisitos

Para desplegar la aplicación necesitas:

* **Terraform** - [Instalar Terraform](https://developer.hashicorp.com/terraform/downloads)
* **AWS CLI** - [Instalar AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html)
* **Java 21** - [Instalar Java 21](https://docs.aws.amazon.com/corretto/latest/corretto-21-ug/downloads-list.html)
* **Maven** - [Instalar Maven](https://maven.apache.org/install.html)
* **Credenciales AWS** configuradas

## Comandos Globales

### Construir todos los módulos
```bash
mvn clean package
```

### Ejecutar todos los tests
```bash
mvn test
```

### Generar reportes de cobertura
```bash
mvn verify
```

### Desplegar infraestructura

```bash
# 1. Construir todos los módulos
mvn clean package

# 2. Desplegar infraestructura con Terraform
cd terraform
terraform init
terraform plan
terraform apply
```

#### Proceso de Despliegue

1. **Construcción**: Maven compila y empaqueta las funciones Lambda
2. **Planificación**: Terraform muestra los cambios a realizar
3. **Aplicación**: Terraform despliega/actualiza la infraestructura AWS

**Recursos desplegados:**
- API Gateway HTTP API
- Funciones Lambda con SnapStart
- DynamoDB tables
- IAM roles y políticas
- SSM parameters
- CloudWatch logs

La URL del API Gateway se muestra en los outputs de Terraform.

## Desarrollo Local

Cada módulo se puede desarrollar y probar independientemente:

### Testing Unitario
```bash
# Ejecutar tests de un módulo específico
cd authentication-service
mvn test

# Ejecutar tests de todos los módulos
mvn test
```

### Testing E2E
Utiliza los tests HTTP en `acceptance-testing/` con IntelliJ HTTP Client:
- Configura el entorno en `http-client.env.json`
- Ejecuta requests desde archivos `.http`
- Tests automáticos con JavaScript post-request

### Variables de Entorno
Las funciones Lambda utilizan variables configuradas en Terraform:
- `MAIN_CLASS`: Clase principal de Spring Boot
- `SPRING_CLOUD_FUNCTION_DEFINITION`: Función a ejecutar
- `JWT_EXPIRATION`: Tiempo de expiración de tokens
- `SPRING_CLOUD_AWS_REGION_STATIC`: Región AWS

## Añadir Recursos a la Aplicación

La infraestructura se define usando **Terraform** con módulos reutilizables:

- **Módulo lambda-function**: Para nuevas funciones Lambda
- **Módulo shared-infrastructure**: Para recursos compartidos
- **Configuración por servicio**: En `terraform/{service}/`

Para añadir un nuevo servicio:
1. Crear directorio en `terraform/{nuevo-servicio}/`
2. Definir recursos usando el módulo lambda-function
3. Añadir al módulo principal en `terraform/main.tf`

## Logs y Monitorización

Todas las funciones Lambda tienen logs automáticos en CloudWatch:

```bash
# Ver logs con AWS CLI
aws logs tail /aws/lambda/authentication-service-login --follow

# Ver logs de un período específico
aws logs filter-log-events --log-group-name /aws/lambda/authentication-service-login --start-time 1640995200000
```

**Monitorización incluida:**
- CloudWatch Logs automáticos
- Métricas de Lambda (duración, errores, invocaciones)
- SnapStart habilitado para cold start optimization
- Tags para organización en AWS Service Catalog

## Estructura del Proyecto

```
serverless-back/
├── authentication-service/     # Plataforma de identidad (✅ Completo)
│   ├── src/                    # Código fuente y tests
│   ├── events/                 # Eventos de prueba JSON
│   ├── pom.xml                 # Configuración Maven
│   └── README.md               # Documentación del servicio
├── user-management-service/    # Plataforma de gestión de usuarios (🚧 En desarrollo)
│   ├── src/                    # Código fuente y tests
│   ├── events/                 # Eventos de prueba JSON
│   ├── pom.xml                 # Configuración Maven
│   └── README.md               # Documentación del servicio
├── cloud-infrastructure-commons/ # Plataforma común cloud
│   ├── src/                    # Configuraciones y utilidades
│   ├── pom.xml                 # Configuración Maven
│   └── README.md               # Documentación del módulo
├── domain-commons/             # Plataforma de dominio compartido
│   ├── src/                    # DTOs, puertos y casos de uso
│   ├── pom.xml                 # Configuración Maven
│   └── README.md               # Documentación del módulo
├── api-documentation-service/  # Servicio de documentación
│   ├── src/                    # Agregación y generación
│   ├── pom.xml                 # Configuración Maven
│   └── README.md               # Documentación del servicio
├── terraform/                  # Infrastructure as Code
│   ├── modules/                # Módulos reutilizables
│   ├── authentication-service/ # Configuración authentication-service
│   ├── main.tf                 # Configuración principal
│   └── README.md               # Documentación de Terraform
├── acceptance-testing/         # Tests de aceptación
│   ├── by-service/             # Tests organizados por servicio
│   ├── http-client.env.json    # Configuración de entornos
│   └── README.md               # Documentación de tests
├── .github/workflows/          # CI/CD con GitHub Actions
├── pom.xml                     # POM padre con configuración común
└── README.md                   # Este archivo
```

## Limpieza

Para eliminar todos los recursos desplegados:

```bash
# Eliminar infraestructura completa
cd terraform
terraform destroy
```

**Nota**: Terraform eliminará todos los recursos de forma ordenada, incluyendo:
- Funciones Lambda
- API Gateway
- DynamoDB tables
- IAM roles
- CloudWatch logs
- SSM parameters

## Calidad del Código

### Cobertura de Tests
El proyecto mantiene una cobertura de tests superior al 90% en todas las métricas:
- **Cobertura de líneas**: >90%
- **Cobertura de métodos**: >90%
- **Cobertura de ramas**: >90%

[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=pedroenlanube_serverless-back&metric=coverage)](https://sonarcloud.io/summary/new_code?id=pedroenlanube_serverless-back) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=pedroenlanube_serverless-back&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=pedroenlanube_serverless-back)

### Tipos de Tests
- **Tests Unitarios**: Verifican lógica de negocio con mocks (sin dependencias externas)
- **Tests E2E**: Validación completa contra infraestructura desplegada

## Troubleshooting

### Problemas Comunes

#### Error: NoSuchBeanDefinitionException: FunctionCatalog
**Solución:**
```java
// Asegurar que está presente en Configuration class:
@Import(ContextFunctionCatalogAutoConfiguration.class)
```

#### Error: Lambda timeout
**Solución:**
- Verificar configuración de timeout en Terraform (default: 30s)
- Revisar logs en CloudWatch para identificar cuellos de botella
- Optimizar cold start con SnapStart (ya habilitado)

#### Error: DynamoDB access denied
**Solución:**
- Verificar IAM policies en módulo Terraform
- Confirmar que la tabla existe y tiene el nombre correcto
- Revisar tags de recursos para AppRegistry

#### Error: JWT secret not found
**Solución:**
```bash
# Verificar que el parámetro existe en SSM
aws ssm get-parameter --name "/login/jwt/secret" --with-decryption
```

### Debugging

#### Ver logs de Lambda
```bash
# Logs en tiempo real
aws logs tail /aws/lambda/authentication-service-login --follow

# Logs con filtro
aws logs filter-log-events --log-group-name /aws/lambda/authentication-service-login --filter-pattern "ERROR"
```

#### Testing local
```bash
# Ejecutar tests con debug
mvn test -Dtest="LoginUserServiceUnitTest" -X

# Verificar configuración Spring
mvn spring-boot:run -Dspring.profiles.active=local
```

## Recursos Adicionales

- [Terraform AWS Provider](https://registry.terraform.io/providers/hashicorp/aws/latest/docs) - Documentación oficial
- [Spring Cloud Function](https://spring.io/projects/spring-cloud-function) - Framework para funciones serverless
- [AWS Lambda Java](https://docs.aws.amazon.com/lambda/latest/dg/lambda-java.html) - Guía oficial de Lambda con Java
- [DynamoDB Developer Guide](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/) - Documentación de DynamoDB
- [API Gateway HTTP APIs](https://docs.aws.amazon.com/apigateway/latest/developerguide/http-api.html) - Documentación de API Gateway v2
