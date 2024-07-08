package com.example.inventory_management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity()
@RequiredArgsConstructor
public class SecurityConfiguration {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  private final AuthenticationProvider authenticationProvider;
  
  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
      .csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/api/auth/current").authenticated()
        .anyRequest().permitAll()
      )
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authenticationProvider(authenticationProvider)
      .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
      .build();
  }
}
