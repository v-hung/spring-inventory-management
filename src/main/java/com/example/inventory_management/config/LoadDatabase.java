package com.example.inventory_management.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.inventory_management.model.auth.Role;
import com.example.inventory_management.model.auth.User;
import com.example.inventory_management.repository.RoleRepository;
import com.example.inventory_management.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class LoadDatabase {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private final RoleRepository roleRepository;

  @Bean
  CommandLineRunner initDatabase() {
    return args -> {
      List<Role> roles = Arrays.asList(
        Role.builder().name("product:read").build(),
        Role.builder().name("product:update").build()
      );

      List<Role> oldRoles = roleRepository.findAll();

      roles = roleRepository.saveAll(roles);

      User admin = userRepository.findByEmail("admin@admin.com").orElse(null);

      if (admin == null) {

        admin = User.builder()
          .email("admin@admin.com")
          .name("Admin")
          .password(passwordEncoder.encode("Admin123!"))
          .roles(new HashSet<Role>(roles))
          .build();

        userRepository.save(admin);
      }
      else {
        admin.setRoles(new HashSet<Role>(roles));

        userRepository.save(admin);
      }

      roleRepository.deleteAllById(oldRoles.stream().map(e -> e.getId()).collect(Collectors.toList()));
    };
  }
}
