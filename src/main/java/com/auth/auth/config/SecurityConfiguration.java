package com.auth.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;



import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtRequestFilter jwtRequestFilter;
    private final UserDetailsService userDetailsService;

    public SecurityConfiguration(JwtRequestFilter jwtAuthFilter, UserDetailsService userDetailsService) {
        this.jwtRequestFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

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
            "/users/register",
            "/users/login",
            "/h2-console/**",
            "/actuator/**"
    };

    /*
     * // http.csrf(csrf -> csrf.disable())
     * // .authorizeHttpRequests(authz -> authz
     * // .anyRequest().authenticated()
     * // )
     * // .formLogin(form -> form
     * // .permitAll()
     * // )
     * // .httpBasic(withDefaults());
     * //
     * // return http.build();
     */

    @Bean
    SecurityFilterChain filterCahin(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(NO_AUTH_WHITELIST).permitAll()
                        // .requestMatchers(HttpMethod.POST).authenticated()
                        .requestMatchers(HttpMethod.GET).authenticated()
                        .anyRequest().authenticated())
                // .formLogin(withDefaults())
                // .httpBasic(withDefaults());
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permitir orígenes específicos (ajusta según tus necesidades)
         configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000",    // React
            "http://localhost:4200",    // Angular
            "http://localhost:8080",    // Mismo servidor (Swagger)
            "http://localhost:8083",    // Mismo servidor (Swagger)
            "http://127.0.0.1:8080",    // Alternativa localhost
             "http://127.0.0.1:8083" 
        ));
        // Permitir métodos HTTP específicos
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));
        // Permitir encabezados específicos
         configuration.setAllowedHeaders(Arrays.asList(
            "Origin",
            "Accept", 
            "X-Requested-With",
            "Access-Control-Allow-Origin",
            "Authorization",
            "Cache-Control",
            "Content-Type",
            "Access-Control-Allow-Headers",
            "Access-Control-Allow-Methods"
        ));;
        // Permitir credenciales (si es necesario, por ejemplo, para cookies o
        // autenticación)

          //Headers expuestos al cliente
        configuration.setExposedHeaders(Arrays.asList(
            "Authorization",
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials"
        ));
        configuration.setAllowCredentials(true);
        // Establecer tiempo de caché para preflight requests (en segundos)
        configuration.setMaxAge(3600L);

        // Registrar la configuración para todas las rutas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
