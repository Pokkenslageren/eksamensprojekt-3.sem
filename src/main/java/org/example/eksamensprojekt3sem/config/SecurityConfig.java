package org.example.eksamensprojekt3sem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    //overrider spring security, så vi kan tilgå h2 med standard credentials
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/h2-console/**", "/api/**").permitAll() // Allow H2 console and API
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**", "/api/**") // Disable CSRF for H2 console and API
                )
                .headers(headers -> headers
                        .frameOptions().sameOrigin() // Allow frames for H2 console
                )
                .formLogin(withDefaults()); // or .httpBasic();

        return http.build();
    }
}
