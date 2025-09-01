output "api_url" {
  description = "API Gateway URL"
  value       = module.shared_infrastructure.api_url
}

output "api_id" {
  description = "API Gateway ID"
  value       = module.shared_infrastructure.api_id
}

output "users_table_name" {
  description = "Users table name"
  value       = module.shared_infrastructure.users_table_name
}

output "login_function_arn" {
  description = "Login function ARN"
  value       = module.auth_manager.login_function_arn
}