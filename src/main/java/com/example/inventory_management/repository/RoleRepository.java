package com.example.inventory_management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.inventory_management.model.auth.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
  Role findByName(String name);
}
