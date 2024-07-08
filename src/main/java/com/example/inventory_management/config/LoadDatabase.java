package com.example.inventory_management.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.inventory_management.model.auth.Role;
import com.example.inventory_management.model.auth.User;
import com.example.inventory_management.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class LoadDatabase {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  @Bean
  CommandLineRunner initDatabase() {
    return args -> {
      if (userRepository.findByEmail("admin@admin.com").isEmpty()) {
        User admin = User.builder()
          .email("admin@admin.com")
          .name("Admin")
          .password(passwordEncoder.encode("Admin123!"))
          .role(Role.ADMIN)
          .build();

        userRepository.save(admin);
        
        System.out.println("Admin user created");
      }
    };
  }
}
