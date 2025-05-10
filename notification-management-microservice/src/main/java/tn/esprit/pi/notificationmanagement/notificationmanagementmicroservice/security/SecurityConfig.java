package tn.esprit.pi.notificationmanagement.notificationmanagementmicroservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for APIs (optional)
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS)) // No session (best for APIs)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/users",
                                "/users/login",
                                "/users/verify-otp",
                                "/api/reclamations/**",
                                "/api/notifications/**",
                                "/ws-notifications/**",
                                "/api/notifications/sendWebSocketNotification"
                        ).permitAll()// Allow Swagger UI without login
                        .requestMatchers("/error").permitAll()
                )
                .build();

    }


}
