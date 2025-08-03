# MyServerlessApp

Aplicación serverless modular desarrollada con **Java 21** y **arquitectura hexagonal**. Cada módulo se puede desplegar independientemente usando SAM CLI.

## Stack Tecnológico

- **Java 21** con Spring Cloud Function
- **Maven** para gestión de dependencias
- **AWS Lambda** para funciones serverless
- **DynamoDB** para persistencia
- **API Gateway** para endpoints HTTP
- **AWS Systems Manager** para gestión de secretos
- **JaCoCo** para cobertura de tests (70%)

## Módulos

### 🔐 [Login Module](./login-module/README.md)
Módulo de autenticación que proporciona:
- Validación de credenciales de usuario
- Generación de tokens JWT
- Integración con AWS Systems Manager para secretos

### 👥 [User Management Module](./user-management-module/README.md)
Módulo de gestión de usuarios que incluye:
- Registro de nuevos usuarios
- Actualización de datos de usuario
- Eliminación de usuarios

## Arquitectura

Cada módulo implementa **arquitectura hexagonal**:
- **Domain** - Entidades y reglas de negocio
- **Application** - Casos de uso y servicios
- **Infrastructure** - Adaptadores (web, persistencia, externos)

If you prefer to use an integrated development environment (IDE) to build and test your application, you can use the AWS Toolkit.  
The AWS Toolkit is an open source plug-in for popular IDEs that uses the SAM CLI to build and deploy serverless applications on AWS. The AWS Toolkit also adds a simplified step-through debugging experience for Lambda function code. See the following links to get started.

* [CLion](https://docs.aws.amazon.com/toolkit-for-jetbrains/latest/userguide/welcome.html)
* [GoLand](https://docs.aws.amazon.com/toolkit-for-jetbrains/latest/userguide/welcome.html)
* [IntelliJ](https://docs.aws.amazon.com/toolkit-for-jetbrains/latest/userguide/welcome.html)
* [WebStorm](https://docs.aws.amazon.com/toolkit-for-jetbrains/latest/userguide/welcome.html)
* [Rider](https://docs.aws.amazon.com/toolkit-for-jetbrains/latest/userguide/welcome.html)
* [PhpStorm](https://docs.aws.amazon.com/toolkit-for-jetbrains/latest/userguide/welcome.html)
* [PyCharm](https://docs.aws.amazon.com/toolkit-for-jetbrains/latest/userguide/welcome.html)
* [RubyMine](https://docs.aws.amazon.com/toolkit-for-jetbrains/latest/userguide/welcome.html)
* [DataGrip](https://docs.aws.amazon.com/toolkit-for-jetbrains/latest/userguide/welcome.html)
* [VS Code](https://docs.aws.amazon.com/toolkit-for-vscode/latest/userguide/welcome.html)
* [Visual Studio](https://docs.aws.amazon.com/toolkit-for-visual-studio/latest/user-guide/welcome.html)

## Deploy the sample application

The Serverless Application Model Command Line Interface (SAM CLI) is an extension of the AWS CLI that adds functionality for building and testing Lambda applications. It uses Docker to run your functions in an Amazon Linux environment that matches Lambda. It can also emulate your application's build environment and API.

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
Ver README específico de cada módulo para instrucciones detalladas de despliegue.

The first command will build the source of your application. The second command will package and deploy your application to AWS, with a series of prompts:

* **Stack Name**: The name of the stack to deploy to CloudFormation. This should be unique to your account and region, and a good starting point would be something matching your project name.
* **AWS Region**: The AWS region you want to deploy your app to.
* **Confirm changes before deploy**: If set to yes, any change sets will be shown to you before execution for manual review. If set to no, the AWS SAM CLI will automatically deploy application changes.
* **Allow SAM CLI IAM role creation**: Many AWS SAM templates, including this example, create AWS IAM roles required for the AWS Lambda function(s) included to access AWS services. By default, these are scoped down to minimum required permissions. To deploy an AWS CloudFormation stack which creates or modifies IAM roles, the `CAPABILITY_IAM` value for `capabilities` must be provided. If permission isn't provided through this prompt, to deploy this example you must explicitly pass `--capabilities CAPABILITY_IAM` to the `sam deploy` command.
* **Save arguments to samconfig.toml**: If set to yes, your choices will be saved to a configuration file inside the project, so that in the future you can just re-run `sam deploy` without parameters to deploy changes to your application.

You can find your API Gateway Endpoint URL in the output values displayed after deployment.

## Desarrollo Local

Cada módulo se puede desarrollar y probar independientemente. Consulta el README específico de cada módulo para:
- Comandos de construcción local
- Ejecución de funciones Lambda localmente
- Testing de APIs
- Variables de entorno específicas

The SAM CLI reads the application template to determine the API's routes and the functions that they invoke. The `Events` property on each function's definition includes the route and method for each path.

```yaml
      Events:
        HelloWorld:
          Type: Api
          Properties:
            Path: /hello
            Method: get
```

## Add a resource to your application
The application template uses AWS Serverless Application Model (AWS SAM) to define application resources. AWS SAM is an extension of AWS CloudFormation with a simpler syntax for configuring common serverless application resources such as functions, triggers, and APIs. For resources not included in [the SAM specification](https://github.com/awslabs/serverless-application-model/blob/master/versions/2016-10-31.md), you can use standard [AWS CloudFormation](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-template-resource-type-ref.html) resource types.

## Fetch, tail, and filter Lambda function logs

To simplify troubleshooting, SAM CLI has a command called `sam logs`. `sam logs` lets you fetch logs generated by your deployed Lambda function from the command line. In addition to printing the logs on the terminal, this command has several nifty features to help you quickly find the bug.

`NOTE`: This command works for all AWS Lambda functions; not just the ones you deploy using SAM.

```bash
MyServerlessApp$ sam logs -n HelloWorldFunction --stack-name MyServerlessApp --tail
```

You can find more information and examples about filtering Lambda function logs in the [SAM CLI Documentation](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-logging.html).

## Estructura del Proyecto

```
MyServerlessApp/
├── login-module/           # Módulo de autenticación
│   ├── src/
│   ├── template.yaml
│   ├── pom.xml
│   └── README.md
├── user-management-module/ # Módulo de gestión de usuarios
│   ├── src/
│   ├── template.yaml
│   ├── pom.xml
│   └── README.md
├── events/                 # Eventos de prueba
├── pom.xml                # POM padre
└── README.md              # Este archivo
```

## Limpieza

Para eliminar recursos desplegados, ejecuta `sam delete` en cada módulo:

```bash
cd login-module && sam delete
cd ../user-management-module && sam delete
```

## Resources

See the [AWS SAM developer guide](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/what-is-sam.html) for an introduction to SAM specification, the SAM CLI, and serverless application concepts.

Next, you can use AWS Serverless Application Repository to deploy ready to use Apps that go beyond hello world samples and learn how authors developed their applications: [AWS Serverless Application Repository main page](https://aws.amazon.com/serverless/serverlessrepo/)
