package com.project.vango.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class OpenApiConfig {

        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("VanGo API")
                                                .version("1.0")
                                                .description("""
                                                                API para el sistema de gestión de VanGo

                                                                ## Autenticación
                                                                La API utiliza JWT para la autenticación.
                                                                Incluye el token en el header de las peticiones:
                                                                `Authorization: Bearer <token>`

                                                                ## Usuarios
                                                                El administrador tiene acceso completo a todas las funcionalidades de la aplicación.
                                                                El cliente tiene el poder de gestionar sus propias reservas y reseñas, además de la configuración de su propio perfil de usuario.
                                                                """)
                                                .license(new License()
                                                                .name("Apache 2.0")
                                                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                                .servers(List.of(
                                                new Server()
                                                                .url("http://localhost:8080")
                                                                .description("Servidor de desarrollo"),
                                                new Server()
                                                                .url("https://api.vango.com")
                                                                .description("Servidor de producción")))
                                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                                .components(new io.swagger.v3.oas.models.Components()
                                                .addSecuritySchemes("bearerAuth",
                                                                new SecurityScheme()
                                                                                .type(SecurityScheme.Type.HTTP)
                                                                                .scheme("bearer")
                                                                                .bearerFormat("JWT")
                                                                                .description("JWT token de autenticación")));
        }
}