package com.example.inventory_management.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.example.inventory_management.model.auth.DeviceMetadata;
import com.example.inventory_management.model.auth.User;
import com.example.inventory_management.repository.DeviceMetadataRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import ua_parser.Client;
import ua_parser.Parser;

@Service
@RequiredArgsConstructor
public class DeviceMetadataService {
  private final DeviceMetadataRepository deviceMetadataRepository;

  public DeviceMetadata findDeviceMetadataByUser(HttpServletRequest request, User user) {
    Parser uaParser = new Parser();
    Client c = uaParser.parse(request.getHeader("user-agent"));

    System.err.println(request.getRemoteAddr());

    DeviceMetadata deviceMetadata = deviceMetadataRepository
      .findByUserAndIpAndUserAgentAndDeviceAndOs(user, request.getRemoteAddr(), c.userAgent.family, c.device.family, c.os.family);
    
    return deviceMetadata;
  }

  public void createDeviceMetadataForUser(HttpServletRequest request, User user, String refreshToken, LocalDateTime expiryTime) {
    Parser uaParser = new Parser();
    Client c = uaParser.parse(request.getHeader("user-agent"));

    DeviceMetadata deviceMetadata = DeviceMetadata.builder()
      .user(user)
      .userAgent(c.userAgent.family)
      .device(c.device.family)
      .os(c.os.family)
      .refreshToken(refreshToken)
      .expiryTime(expiryTime)
      .lastLoggedIn(LocalDateTime.now())
      .ip(request.getRemoteAddr())
      .build();

    deviceMetadataRepository.save(deviceMetadata);
  }

  public DeviceMetadata findByRefreshToken(String token) {
    return deviceMetadataRepository.findByRefreshToken(token)
      .orElseThrow(() -> new RuntimeException("RefreshToken is not found"));
  }

  public void deleteByRefreshToken(String token) {
    deviceMetadataRepository.deleteByRefreshToken(token);
  }

  public void deleteByExpiryTimeExpired(User user) {
    deviceMetadataRepository.deleteByExpiryTimeBeforeAndUser(LocalDateTime.now(), user);
  }
}
