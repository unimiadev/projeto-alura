package br.com.alura.projeto.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Projeto Alura - API Documentation")
                        .version("1.0.0")
                        .description("API completa para gerenciamento de cursos, matrículas e relatórios da plataforma Alura")
                        .contact(new Contact()
                                .name("Equipe Alura")
                                .email("contato@alura.com.br")
                                .url("https://alura.com.br"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de Desenvolvimento"),
                        new Server()
                                .url("https://api.alura.com.br")
                                .description("Servidor de Produção (Exemplo)")
                ));
    }
}
