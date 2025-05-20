package org.example.eksamensprojekt3sem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)  // Disable CSRF for simplicity during development
                .authorizeHttpRequests(auth -> auth
                        // Allow access to static resources
                        .requestMatchers("/", "/index.html", "/css/**", "/js/**", "/*.js", "/*.css", "/images/**").permitAll()
                        // Allow access to H2 console
                        .requestMatchers("/h2-console/**").permitAll()
                        // Allow access to API endpoints
                        .requestMatchers("/fodboldklub/**").permitAll()
                        // Any other request requires authentication
                        .anyRequest().permitAll()  // Change to authenticated() when you implement authentication
                )
                .headers(headers -> headers
                        .frameOptions().sameOrigin()  // Allow frames for H2 console
                )
                // Disable form login and HTTP Basic for now
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
