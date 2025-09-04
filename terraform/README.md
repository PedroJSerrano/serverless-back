# Terraform Infrastructure

Infrastructure as Code (IaC) para el despliegue de la aplicación serverless en AWS usando Terraform.

## Estructura

```
terraform/
├── modules/                    # Módulos reutilizables
│   ├── lambda-function/        # Módulo para funciones Lambda
│   └── shared-infrastructure/  # Recursos compartidos (DynamoDB, API Gateway)
├── auth-manager/              # Configuración específica auth-manager
│   ├── main.tf
│   ├── variables.tf
│   └── outputs.tf
├── main.tf                    # Configuración principal
├── variables.tf               # Variables globales
├── outputs.tf                 # Outputs del proyecto
└── README.md                  # Este archivo
```

## Recursos Desplegados

### Infraestructura Compartida
- **API Gateway HTTP API v2** - Endpoint principal de la aplicación
- **DynamoDB Table** - Tabla `users` para almacenamiento
- **AppRegistry Application** - Organización de recursos AWS
- **CloudWatch Log Groups** - Logs automáticos para todas las funciones

### Auth Manager
- **Lambda Function** - `auth-manager-login` con SnapStart habilitado
- **API Gateway Integration** - Route `/login` POST
- **IAM Roles** - Permisos para DynamoDB y SSM
- **SSM Parameter** - Secreto JWT

## Backend Remoto

El estado de Terraform se almacena en S3:
- **Bucket**: `pedroenlanube-serverless-app-terraform-state`
- **Key**: `serverless-back/terraform.tfstate`
- **Región**: `eu-west-1`

## Variables de Configuración

### Variables Principales
- `aws_region` - Región AWS (default: eu-west-1)
- `environment` - Entorno de despliegue (default: dev)
- `project_name` - Nombre del proyecto (default: serverless-fullstack-app)

### Variables por Módulo
Cada módulo tiene sus propias variables específicas definidas en `variables.tf`.

## Comandos de Despliegue

### Inicialización
```bash
# Inicializar Terraform (solo primera vez)
terraform init
```

### Planificación
```bash
# Ver cambios a realizar
terraform plan
```

### Despliegue
```bash
# Aplicar cambios
terraform apply

# Aplicar sin confirmación
terraform apply -auto-approve
```

### Destrucción
```bash
# Eliminar todos los recursos
terraform destroy
```

## Módulos Reutilizables

### lambda-function
Módulo para crear funciones Lambda con configuración estándar:
- Runtime Java 21
- SnapStart habilitado
- Integración con API Gateway
- IAM roles automáticos
- CloudWatch logs
- Tags consistentes

### shared-infrastructure
Recursos compartidos entre todos los servicios:
- API Gateway HTTP API
- DynamoDB tables
- AppRegistry application
- Configuración de tags

## Tags Automáticos

Todos los recursos incluyen tags consistentes:
- `awsApplication` - Nombre del proyecto
- `Environment` - Entorno (dev/pre/pro)
- `Owner` - pedroenlanube
- `Architecture` - hexagonal
- `applicationName` - serverless-fullstack-app

## Optimizaciones

### Lambda
- **SnapStart** habilitado para reducir cold starts
- **Memory** optimizada por función (512MB default)
- **Timeout** configurado apropiadamente (30s default)
- **Environment variables** específicas por función

### Costos
- **DynamoDB** en modo Pay-per-Request
- **API Gateway** HTTP API (más económico que REST API)
- **CloudWatch** logs con retención automática

## Añadir Nuevos Servicios

Para añadir un nuevo servicio:

1. **Crear directorio** `terraform/{nuevo-servicio}/`
2. **Definir recursos** usando el módulo `lambda-function`
3. **Añadir al main.tf** principal
4. **Configurar variables** específicas

Ejemplo:
```hcl
module "nuevo_servicio" {
  source = "./nuevo-servicio"
  
  api_id = module.shared_infrastructure.api_id
  api_execution_arn = module.shared_infrastructure.api_execution_arn
  environment = var.environment
  project_name = var.project_name
}
```

## Troubleshooting

### Error: Backend initialization
```bash
# Verificar credenciales AWS
aws sts get-caller-identity

# Re-inicializar backend
terraform init -reconfigure
```

### Error: State lock
```bash
# Forzar unlock (usar con cuidado)
terraform force-unlock LOCK_ID
```

### Error: Resource already exists
```bash
# Importar recurso existente
terraform import aws_resource.name resource-id
```