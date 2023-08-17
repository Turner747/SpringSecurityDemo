package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    @Order(1)
    SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/api/**")
                .authorizeHttpRequests( auth -> {
                    auth.anyRequest().authenticated();
                })
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .httpBasic(withDefaults())
                .build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain h2ConsoleSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher(AntPathRequestMatcher.antMatcher("/h2-console/**"))
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**"))
                            .permitAll();
                })
                .csrf(csrf -> {
                    csrf.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**"));
                })
                .headers(headers -> {
                    headers.frameOptions(withDefaults()).disable();
                })
                .build();
    }

    @Bean
    @Order(3)
    SecurityFilterChain formSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(AntPathRequestMatcher.antMatcher("/")).permitAll();
                    auth.anyRequest().authenticated();
                })
                .formLogin(withDefaults())
                .build();
    }
}
