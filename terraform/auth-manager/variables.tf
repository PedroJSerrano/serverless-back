variable "api_id" {
  description = "API Gateway ID"
  type        = string
}

variable "api_execution_arn" {
  description = "API Gateway execution ARN"
  type        = string
}

variable "users_table_name" {
  description = "Users table name"
  type        = string
}

variable "users_table_arn" {
  description = "Users table ARN"
  type        = string
}

variable "environment" {
  description = "Environment name"
  type        = string
}

variable "project_name" {
  description = "Project name"
  type        = string
}