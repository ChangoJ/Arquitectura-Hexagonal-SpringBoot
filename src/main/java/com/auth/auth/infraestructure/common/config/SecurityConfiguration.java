package com.auth.auth.infraestructure.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

     private static final String[] NO_AUTH_WHITELIST = {
            // Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            // Swagger UI v3 (OpenAPI 3)
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            // Otros endpoints públicos
            "/login",
            "/h2-console/**",
            "/actuator/**"
    };

          /* 
         // http.csrf(csrf -> csrf.disable())
         // .authorizeHttpRequests(authz -> authz
         // .anyRequest().authenticated()
         // )
         // .formLogin(form -> form
         // .permitAll()
         // )
         // .httpBasic(withDefaults());
         // 
         // return http.build();
         */

    @Bean
    SecurityFilterChain filterCahin(HttpSecurity http) throws Exception {
  

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(NO_AUTH_WHITELIST).permitAll()
                        .requestMatchers(HttpMethod.POST).authenticated()
                        .requestMatchers(HttpMethod.GET).hasRole("ADMIN")
                        .anyRequest().authenticated())
                .formLogin(withDefaults())
                .httpBasic(withDefaults());

        return http.build();

    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
