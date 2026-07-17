package com.auth.demo.config;

import com.auth.demo.security.JwtAuthFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // ❌ disable CSRF (stateless API)
            .csrf(csrf -> csrf.disable())

            // ❌ no session (JWT only)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // 🌍 CORS (important for frontend)
            .cors(cors -> {})

            // 🔐 Authorization rules
            .authorizeHttpRequests(auth -> auth

                // PUBLIC AUTH ROUTES
                .requestMatchers("/api/v1/auth/**").permitAll()

                // SWAGGER (optional but common)
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                // PUBLIC GET endpoints (optional)
                .requestMatchers(HttpMethod.GET, "/public/**").permitAll()

                // EVERYTHING ELSE SECURED
                .anyRequest().authenticated()
            )

            // 🔥 JWT FILTER
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

            // ❌ HANDLE UNAUTHORIZED (401)
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");

                    response.getWriter().write("""
                        {
                          "statusCode": 401,
                          "message": "Unauthorized",
                          "error": "Authentication required"
                        }
                    """);
                })

                // ❌ HANDLE FORBIDDEN (403)
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("application/json");

                    response.getWriter().write("""
                        {
                          "statusCode": 403,
                          "message": "Forbidden",
                          "error": "You don't have permission"
                        }
                    """);
                })
            );

        return http.build();
    }

    // 🔑 Password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}