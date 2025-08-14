# MyServerlessApp

Aplicación serverless modular desarrollada con **Java 21** y **arquitectura hexagonal**. Cada módulo se puede desplegar independientemente usando SAM CLI.

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
- **TestContainers 1.19.8 + LocalStack** para tests de integración

## Módulos

### 🔐 [Auth Manager](./auth-manager/README.md) ✅ **Completo**
Módulo de autenticación que proporciona:
- Validación de credenciales contra DynamoDB
- Generación de tokens JWT con expiración configurable
- Integración con AWS Systems Manager para secretos
- API REST: `POST /api/login`
- 🚧 **Lambda Authorizer** para recursos privados (en construcción)

**Tecnologías:** Spring Cloud Function, DynamoDB, SSM, JWT

### 👥 [User Manager](./user-manager/README.md) 🚧 **En desarrollo**
Módulo de gestión de usuarios que incluirá:
- Registro de nuevos usuarios (CRUD)
- Actualización de datos de usuario
- Eliminación de usuarios
- Validaciones de negocio

**Tecnologías:** Spring Cloud Function, DynamoDB, JWT

### 📊 [Database MySQL](./database-mysql/README.md) 📄 **Utilidades** 🚧 **En desarrollo**
Módulo de gestión de base de datos que proporciona:
- Scripts de migración con Liquibase 4.33.0
- Versionado de esquemas de base de datos
- Conector MySQL 9.3.0

**Tecnologías:** Liquibase, MySQL Connector

## Arquitectura

Cada módulo implementa **arquitectura hexagonal**:
- **Domain** - Entidades y reglas de negocio
- **Application** - Casos de uso y servicios
- **Infrastructure** - Adaptadores (web, persistencia, externos)

## Herramientas de Desarrollo

Si prefieres usar un entorno de desarrollo integrado (IDE) para construir y probar tu aplicación, puedes usar el AWS Toolkit. El AWS Toolkit es un plugin de código abierto para IDEs populares que usa SAM CLI para construir y desplegar aplicaciones serverless en AWS. También añade una experiencia simplificada de depuración paso a paso para el código de funciones Lambda.

**IDEs compatibles:**
* [IntelliJ IDEA](https://docs.aws.amazon.com/toolkit-for-jetbrains/latest/userguide/welcome.html) (recomendado)
* [VS Code](https://docs.aws.amazon.com/toolkit-for-vscode/latest/userguide/welcome.html)
* [PyCharm](https://docs.aws.amazon.com/toolkit-for-jetbrains/latest/userguide/welcome.html)
* [WebStorm](https://docs.aws.amazon.com/toolkit-for-jetbrains/latest/userguide/welcome.html)
* [Visual Studio](https://docs.aws.amazon.com/toolkit-for-visual-studio/latest/user-guide/welcome.html)

## Despliegue de la Aplicación

El Serverless Application Model Command Line Interface (SAM CLI) es una extensión del AWS CLI que añade funcionalidad para construir y probar aplicaciones Lambda. Usa Docker para ejecutar tus funciones en un entorno Amazon Linux que coincide con Lambda. También puede emular el entorno de construcción y API de tu aplicación.

### Prerrequisitos

Para usar SAM CLI necesitas las siguientes herramientas:

* **SAM CLI** - [Instalar SAM CLI](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-install.html)
* **Java 21** - [Instalar Java 21](https://docs.aws.amazon.com/corretto/latest/corretto-21-ug/downloads-list.html)
* **Maven** - [Instalar Maven](https://maven.apache.org/install.html)
* **Docker** - [Instalar Docker](https://hub.docker.com/search/?type=edition&offering=community)

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

### Desplegar módulos

Cada módulo se despliega independientemente. Ver README específico de cada módulo para instrucciones detalladas.

#### Proceso de Despliegue

El primer comando construirá el código fuente de tu aplicación. El segundo comando empaquetará y desplegará tu aplicación en AWS, con una serie de preguntas:

* **Stack Name**: El nombre del stack para desplegar en CloudFormation. Debe ser único en tu cuenta y región.
* **AWS Region**: La región AWS donde quieres desplegar tu aplicación.
* **Confirm changes before deploy**: Si se establece en sí, cualquier conjunto de cambios se mostrará antes de la ejecución para revisión manual.
* **Allow SAM CLI IAM role creation**: Muchas plantillas de AWS SAM crean roles IAM requeridos para que las funciones Lambda accedan a servicios AWS.
* **Save arguments to samconfig.toml**: Si se establece en sí, tus opciones se guardarán en un archivo de configuración.

Puedes encontrar la URL del endpoint de API Gateway en los valores de salida mostrados después del despliegue.

## Desarrollo Local

Cada módulo se puede desarrollar y probar independientemente. Consulta el README específico de cada módulo para:
- Comandos de construcción local
- Ejecución de funciones Lambda localmente
- Testing de APIs
- Variables de entorno específicas

SAM CLI lee la plantilla de aplicación para determinar las rutas de la API y las funciones que invocan. La propiedad `Events` en la definición de cada función incluye la ruta y método para cada path.

**Ejemplo de configuración de evento:**
```yaml
      Events:
        LoginApi:
          Type: HttpApi
          Properties:
            Path: /login
            Method: POST
```

## Añadir Recursos a tu Aplicación

La plantilla de aplicación usa AWS Serverless Application Model (AWS SAM) para definir recursos de aplicación. AWS SAM es una extensión de AWS CloudFormation con una sintaxis más simple para configurar recursos comunes de aplicaciones serverless como funciones, triggers y APIs.

Para recursos no incluidos en [la especificación SAM](https://github.com/awslabs/serverless-application-model/blob/master/versions/2016-10-31.md), puedes usar tipos de recursos estándar de [AWS CloudFormation](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-template-resource-type-ref.html).

## Obtener y Filtrar Logs de Funciones Lambda

Para simplificar la resolución de problemas, SAM CLI tiene un comando llamado `sam logs`. Este comando te permite obtener logs generados por tu función Lambda desplegada desde la línea de comandos.

**Nota**: Este comando funciona para todas las funciones AWS Lambda, no solo las que despliegas usando SAM.

```bash
# Obtener logs en tiempo real
sam logs -n LoginFunction --stack-name auth-manager --tail

# Obtener logs de un período específico
sam logs -n LoginFunction --stack-name auth-manager --start-time '10min ago'
```

Puedes encontrar más información y ejemplos sobre filtrado de logs de funciones Lambda en la [Documentación de SAM CLI](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-logging.html).

## Estructura del Proyecto

```
MyServerlessApp/
├── auth-manager/           # Módulo de autenticación (✅ Completo)
│   ├── src/                # Código fuente y tests
│   ├── events/             # Eventos de prueba JSON
│   ├── template.yaml       # Plantilla SAM
│   ├── pom.xml            # Configuración Maven
│   └── README.md          # Documentación del módulo
├── user-manager/           # Módulo de gestión de usuarios (🚧 En desarrollo)
│   ├── src/               # Código fuente y tests
│   ├── template.yaml      # Plantilla SAM
│   ├── pom.xml           # Configuración Maven
│   └── README.md         # Documentación del módulo
├── database-mysql/         # Módulo de migraciones de BD (📄 Utilidades)
│   ├── src/               # Scripts Liquibase
│   ├── pom.xml           # Dependencias Liquibase y MySQL
│   └── README.md         # Documentación del módulo
├── .github/workflows/      # CI/CD con GitHub Actions
├── pom.xml                # POM padre con configuración común
└── README.md              # Este archivo
```

## Limpieza

Para eliminar recursos desplegados, ejecuta `sam delete` en cada módulo:

```bash
# Eliminar recursos de auth-manager
cd auth-manager && sam delete

# Eliminar recursos de user-manager (cuando esté desplegado)
cd ../user-manager && sam delete

# database-mysql no requiere limpieza (solo contiene scripts)
```

## Calidad del Código

### Cobertura de Tests
El proyecto mantiene una cobertura de tests superior al 90% en todas las métricas:
- **Cobertura de líneas**: >90%
- **Cobertura de métodos**: >90%
- **Cobertura de ramas**: >90%

### Tipos de Tests
- **Tests Unitarios**: Verifican lógica de negocio con mocks (sin dependencias externas)
- **Tests de Integración**: Verifican configuración Spring con mocks (TestContainers + LocalStack)
- **Tests de Producción**: Verifican comportamiento con servicios AWS reales

## Recursos Adicionales

Consulta la [guía de desarrollador de AWS SAM](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/what-is-sam.html) para una introducción a la especificación SAM, SAM CLI y conceptos de aplicaciones serverless.

También puedes usar AWS Serverless Application Repository para desplegar aplicaciones listas para usar: [AWS Serverless Application Repository](https://aws.amazon.com/serverless/serverlessrepo/)
