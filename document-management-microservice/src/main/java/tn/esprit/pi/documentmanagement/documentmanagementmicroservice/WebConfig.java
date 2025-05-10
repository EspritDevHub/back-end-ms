package tn.esprit.pi.documentmanagement.documentmanagementmicroservice;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")  // Applique CORS pour tous les endpoints qui commencent par /api/
                .allowedOrigins("http://localhost:4200")  // Permet l'origine localhost:4200 (Angular)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")  // Permet toutes les en-têtes
                .allowCredentials(true);  // Permet d'envoyer des cookies avec les requêtes
    }
}
