package com.example.inventory_management.model.auth;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "deviceMetadata")
public class DeviceMetadata {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String ip;

  private String userAgent;

  private String device;

  private String os;

  private LocalDateTime lastLoggedIn;

  @NotBlank
  private String refreshToken;

  @NotNull
  private LocalDateTime expiryTime;

  @ManyToOne
  private User user;
}
