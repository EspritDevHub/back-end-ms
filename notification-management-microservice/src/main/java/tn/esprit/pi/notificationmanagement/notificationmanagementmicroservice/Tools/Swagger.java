package tn.esprit.pi.notificationmanagement.notificationmanagementmicroservice.Tools;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Swagger {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Reclamation Management API")
                        .version("1.0")
                        .description("API for managing users in the system")
                        .contact(new Contact()
                                .name("Imen")
                                .email("")
                                .url("https://esprit.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org")));
    }
}
