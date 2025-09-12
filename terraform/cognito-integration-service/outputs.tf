output "post_confirmation_function_name" {
  description = "Post-confirmation Lambda function name"
  value       = module.post_confirmation_function.function_name
}

output "post_confirmation_function_arn" {
  description = "Post-confirmation Lambda function ARN"
  value       = module.post_confirmation_function.function_arn
}