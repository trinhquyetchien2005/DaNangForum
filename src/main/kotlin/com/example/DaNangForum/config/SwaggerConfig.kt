package com.example.DaNangForum.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class OpenAPIConfig {

    @Bean
    open fun customOpenAPI(
        @Value("\${openapi.service.title}") title: String,
        @Value("\${openapi.service.version}") version: String,
        @Value("\${openapi.service.server}") serverUrl: String
    ): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title(title)
                    .description("API documentation for Danang Forum")
                    .version(version)
                    .license(
                        License().name("Apache 2.0").url("https://springdoc.org")
                    )
            )
            .servers(listOf(Server().url(serverUrl)))
            .components(
                io.swagger.v3.oas.models.Components().addSecuritySchemes(
                    "bearerAuth", SecurityScheme()
                        .name("Authorization")
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .`in`(SecurityScheme.In.HEADER)
                )
            )
    }
    @Bean
    open fun defaultApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("danang-forum")
            .pathsToMatch("/**")
            .build()
    }

}
