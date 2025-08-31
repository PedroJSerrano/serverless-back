# Data sources
data "aws_region" "current" {}
data "aws_caller_identity" "current" {}

# Auth Manager Login Lambda
module "login_function" {
  source = "../modules/lambda-function"
  
  function_name = "auth-manager-login"
  handler      = "org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest"
  runtime      = "java21"
  timeout      = 30
  memory_size  = 512
  zip_file     = "../auth-manager/target/auth-manager-1.3.0.jar"
  
  api_id            = var.api_id
  api_execution_arn = var.api_execution_arn
  route_path        = "/login"
  http_method       = "POST"
  
  environment_variables = {
    MAIN_CLASS                        = "pjserrano.authmanager.AuthManagerConfiguration"
    SPRING_CLOUD_FUNCTION_DEFINITION  = "login"
    JWT_EXPIRATION                    = "3600000"
    SPRING_CLOUD_AWS_REGION_STATIC    = data.aws_region.current.name
    JAVA_TOOL_OPTIONS                 = "-XX:+UseSerialGC -Xmx512m -XX:+TieredCompilation -XX:TieredStopAtLevel=1"
    SPRING_JMX_ENABLED                = "false"
    SPRING_MAIN_BANNER_MODE           = "off"
  }
  
  custom_policies = [
    {
      Effect = "Allow"
      Action = ["ssm:GetParameter"]
      Resource = "arn:aws:ssm:${data.aws_region.current.name}:${data.aws_caller_identity.current.account_id}:parameter/login/jwt/secret"
    },
    {
      Effect = "Allow"
      Action = ["dynamodb:GetItem"]
      Resource = var.users_table_arn
    }
  ]
  
  tags = {
    awsApplication = var.project_name
    Environment    = var.environment
    Module         = "auth-manager"
    Owner          = "pjserrano"
    Architecture   = "hexagonal"
  }
}