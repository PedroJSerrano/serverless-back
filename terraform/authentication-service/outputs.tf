output "login_function_arn" {
  description = "Login function ARN"
  value       = module.login_function.function_arn
}

output "login_function_name" {
  description = "Login function name"
  value       = module.login_function.function_name
}

output "login_alias_arn" {
  description = "Login function alias ARN"
  value       = module.login_function.alias_arn
}