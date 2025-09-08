# ServiceCatalog AppRegistry Application
resource "aws_servicecatalogappregistry_application" "main" {
  name        = var.project_name
  description = "Aplicación web serverless"
  
  tags = {
    awsApplication = var.project_name
    "user:Environment"    = var.environment
    "user:Owner"          = "pedroenlanube"
    "user:ApplicationName" = "pedroenlanube-serverless-web-dev"
  }
}

# DynamoDB Table
resource "aws_dynamodb_table" "users" {
  name           = "users"
  billing_mode   = "PAY_PER_REQUEST"
  hash_key       = "username"

  attribute {
    name = "username"
    type = "S"
  }

  tags = {
    awsApplication = var.project_name
    "user:Module"         = "shared-infrastructure"
    "user:Environment"    = var.environment
    "user:Owner"          = "pedroenlanube"
    "user:ApplicationName" = "pedroenlanube-serverless-web-dev"
  }
}

# API Gateway HTTP API
resource "aws_apigatewayv2_api" "main" {
  name          = "${var.project_name}-api"
  protocol_type = "HTTP"
  
  tags = {
    awsApplication = var.project_name
    "user:Module"         = "shared-infrastructure"
    "user:Environment"    = var.environment
    "user:Owner"          = "pedroenlanube"
    "user:ApplicationName" = "pedroenlanube-serverless-web-dev"
  }
}

# API Gateway Stage
resource "aws_apigatewayv2_stage" "dev" {
  api_id      = aws_apigatewayv2_api.main.id
  name        = var.environment
  auto_deploy = true
  
  tags = {
    awsApplication = var.project_name
    "user:Module"         = "shared-infrastructure"
    "user:Environment"    = var.environment
    "user:Owner"          = "pedroenlanube"
    "user:ApplicationName" = "pedroenlanube-serverless-web-dev"
  }
}