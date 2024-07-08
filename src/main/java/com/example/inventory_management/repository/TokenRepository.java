package com.example.inventory_management.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.inventory_management.model.auth.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

  Optional<Token> findByRefreshToken(String refreshToken);

  Optional<Token> findByExpiryTimeAfter(LocalDateTime expiryTime);

  void deleteByRefreshToken(String refreshToken);
}
