package com.example.inventory_management.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.inventory_management.model.auth.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
  
  Optional<User> findByEmail(String email);
}
