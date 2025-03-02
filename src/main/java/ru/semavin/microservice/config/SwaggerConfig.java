package ru.semavin.microservice.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация Swagger/OpenAPI через Springdoc (v2.8.5).
 * <p>
 * 1) Bean типа {@link OpenAPI}
 * 2) Bean типа {@link GroupedOpenApi} задаёт группу и пакеты для сканирования контроллеров.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Настраиваем глобальную спецификацию OpenAPI.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Микросервис управления пользователями и подписками")
                        .description("Описание эндпоинтов микросервиса")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Semavin")
                                .email("asemavin250604@gmail.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html"))
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Java документация")
                        .url("http://localhost:8080/docs/javadoc/index.html"));
    }

    /**
     * Группируем API и указываем, где искать контроллеры.
     */
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("v1")
                .packagesToScan("ru.semavin.microservice.controllers")
                .build();
    }
}
