variable "function_name" {
  description = "Name of the Lambda function"
  type        = string
}

variable "handler" {
  description = "Lambda function handler"
  type        = string
}

variable "runtime" {
  description = "Lambda runtime"
  type        = string
}

variable "timeout" {
  description = "Lambda timeout in seconds"
  type        = number
  default     = 30
}

variable "memory_size" {
  description = "Lambda memory size in MB"
  type        = number
  default     = 512
}

variable "zip_file" {
  description = "Path to the Lambda deployment package"
  type        = string
}

variable "environment_variables" {
  description = "Environment variables for the Lambda function"
  type        = map(string)
  default     = {}
}

variable "custom_policies" {
  description = "Custom IAM policies for the Lambda function"
  type        = list(any)
  default     = []
}

variable "api_id" {
  description = "API Gateway ID (optional for Cognito triggers)"
  type        = string
  default     = ""
}

variable "api_execution_arn" {
  description = "API Gateway execution ARN (optional for Cognito triggers)"
  type        = string
  default     = ""
}

variable "route_path" {
  description = "API Gateway route path (optional for Cognito triggers)"
  type        = string
  default     = ""
}

variable "http_method" {
  description = "HTTP method for the route (optional for Cognito triggers)"
  type        = string
  default     = ""
}

variable "tags" {
  description = "Tags to apply to resources"
  type        = map(string)
  default     = {}
}