# Data sources
data "aws_region" "current" {}
data "aws_caller_identity" "current" {}

# Data source para el User Pool existente
data "aws_cognito_user_pool" "existing" {
  name = var.cognito_user_pool_id
}

# Post-Confirmation Lambda Function
module "post_confirmation_function" {
  source = "../modules/lambda-function"
  
  function_name = "cognito-integration-post-confirmation"
  handler      = "org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest"
  runtime      = "java21"
  timeout      = 30
  memory_size  = 512
  zip_file     = "../cognito-integration-service/target/cognito-triggers-1.4.0.jar"
  
  # No API Gateway integration for Cognito triggers
  api_id            = ""
  api_execution_arn = ""
  route_path        = ""
  http_method       = ""
  
  environment_variables = {
    MAIN_CLASS                        = "dev.pedronube.authenticationservice.CognitoIntegrationConfiguration"
    SPRING_CLOUD_FUNCTION_DEFINITION  = "postConfirmation"
    SPRING_CLOUD_AWS_REGION_STATIC    = data.aws_region.current.name
    JAVA_TOOL_OPTIONS                 = "-XX:+UseSerialGC -Xmx512m -XX:+TieredCompilation -XX:TieredStopAtLevel=1"
    SPRING_JMX_ENABLED                = "false"
    SPRING_MAIN_BANNER_MODE           = "off"
  }
  
  custom_policies = [
    {
      Effect = "Allow"
      Action = ["dynamodb:PutItem"]
      Resource = var.users_table_arn
    }
  ]
  
  tags = {
    awsApplication = var.project_name
    "user:Environment"    = var.environment
    "user:Module"         = "cognito-integration-service"
    "user:Owner"          = "pedroenlanube"
    "user:ApplicationName" = "pedroenlanube-serverless-web-dev"
  }
}

# Lambda Permission para Cognito Post-Confirmation
resource "aws_lambda_permission" "cognito_post_confirmation" {
  statement_id  = "AllowCognitoPostConfirmation"
  action        = "lambda:InvokeFunction"
  function_name = module.post_confirmation_function.function_name
  principal     = "cognito-idp.amazonaws.com"
  source_arn    = "arn:aws:cognito-idp:${data.aws_region.current.name}:${data.aws_caller_identity.current.account_id}:userpool/${var.cognito_user_pool_id}"
}