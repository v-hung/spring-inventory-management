package com.example.inventory_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.example.inventory_management.model.auth.DeviceMetadata;
import com.example.inventory_management.model.auth.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface DeviceMetadataRepository extends JpaRepository<DeviceMetadata, Integer> {

  List<DeviceMetadata> findByUser(User user);

  DeviceMetadata findByUserAndIpAndUserAgentAndDeviceAndOs(User user, String ip, String userAgent, String device, String os);

  Optional<DeviceMetadata> findByRefreshToken(String refreshToken);

  // Optional<DeviceMetadata> findByExpiryTimeAfter(LocalDateTime expiryTime);

  @Modifying
  void deleteByRefreshToken(String refreshToken);

  @Modifying
  void deleteByExpiryTimeBeforeAndUser(LocalDateTime expiryTime, User user);
}
