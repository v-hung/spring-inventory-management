package com.example.inventory_management.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.inventory_management.dto.UserDto;
import com.example.inventory_management.model.auth.Role;
import com.example.inventory_management.model.auth.Token;
import com.example.inventory_management.model.auth.User;
import com.example.inventory_management.repository.TokenRepository;
import com.example.inventory_management.repository.UserRepository;
import com.example.inventory_management.request.LoginRequest;
import com.example.inventory_management.request.LogoutRequest;
import com.example.inventory_management.request.RegisterRequest;
import com.example.inventory_management.response.LoginResponse;
import com.example.inventory_management.service.JwtService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final TokenRepository tokenRepository;
  private final ModelMapper modelMapper;

  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
    if (!userRepository.findByEmail(request.getEmail()).isEmpty()) {
      return ResponseEntity.status(400).body("Email đã tồn tại");
    }
    var user = User.builder()
      .name(request.getName())
      .email(request.getEmail())
      .password(passwordEncoder.encode(request.getPassword()))
      .role(Role.USER)
      .build();

    userRepository.save(user);

    return ResponseEntity.ok("Đăng ký người dùng mới thành công");
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
    authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        request.getEmail(),
        request.getPassword()
      )
    );

    var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
    var token = jwtService.generateToken(user);
    
    var refreshTokenInDb = tokenRepository.findByExpiryTimeAfter(LocalDateTime.now()).orElse(null);

    var refreshToken = refreshTokenInDb != null ? refreshTokenInDb.getRefreshToken() : jwtService.generateRefreshToken(user);

    if (refreshTokenInDb == null) {
      var tokenDb = Token.builder()
        .refreshToken(refreshToken)
        .expiryTime(jwtService.getRefreshExpirationToLocalDate())
        .build();
  
      tokenRepository.save(tokenDb);
    }

    LoginResponse response = new LoginResponse(modelMapper.map(user, UserDto.class), token, refreshToken);

    return ResponseEntity.ok(response);
  }

  @PostMapping("/logout")
  public ResponseEntity<String> logout(@RequestBody LogoutRequest request) {
    tokenRepository.deleteByRefreshToken(request.getRefreshToken());

    SecurityContextHolder.clearContext();

    return ResponseEntity.ok("Đăng xuất thành công");
  }

  @GetMapping("/current")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<UserDto> currentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    var user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();

    return ResponseEntity.ok(modelMapper.map(user, UserDto.class));
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<?> refreshToken(@RequestBody LogoutRequest request) {
    tokenRepository.findByRefreshToken(request.getRefreshToken()).orElseThrow(
      () -> new RuntimeException("RefreshToken is not found")
    );

    final String userEmail = jwtService.extractUsername(request.getRefreshToken());
    
    User user = userRepository.findByEmail(userEmail).orElseThrow(
      () -> new RuntimeException("User is not found")
    );

    if (!jwtService.isTokenValid(request.getRefreshToken(), user)) {
      return ResponseEntity.status(401).body("RefreshToken expired");
    }

    var token = jwtService.generateToken(user);

    Map<String, String> response = new HashMap<>();
    response.put("token", token);

    return ResponseEntity.ok(response);
  }
}
