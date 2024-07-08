package com.example.inventory_management.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@OpenAPIDefinition(
  info = @Info(
    contact = @Contact(
      name = "Việt Hùng",
      email = "viet.hung.2898@gmail.com"
    ),
    description = "OpenApi document for Spring Security",
    title = "OpenApi specification - VietHung",
    version = "1.0"
  ),
  security = {
    @SecurityRequirement(name = "bearerAuth")
  }
)
@SecurityScheme(
  name = "bearerAuth",
  description = "Jwt Authentication",
  scheme = "bearer",
  type = SecuritySchemeType.HTTP,
  bearerFormat = "JWT",
  in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
  
}