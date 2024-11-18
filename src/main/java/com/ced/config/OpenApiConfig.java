package com.ced.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${swagger.server}")
    private String serverUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        Server server = new Server();
        server.setUrl(serverUrl);

        return new OpenAPI()
                .info(new Info()
                        .title("API de Magias D&D")
                        .version("v1")
                        .description("API que fornece informações sobre magias de Dungeons & Dragons (DRS)")
                        .license(new License().name("Git Repo").url("https://github.com/Cavernas-e-Dragoes")))
                .servers(List.of(server));
    }
}
