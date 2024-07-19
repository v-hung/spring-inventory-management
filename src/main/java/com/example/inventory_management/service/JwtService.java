package com.example.inventory_management.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  @Value("${spring.application.security.jwt.secret-key}")
  private String secretKey;

  @Value("${spring.application.security.jwt.expiration}")
  private long jwtExpiration;

  @Value("${spring.application.security.jwt.refresh-token.expiration}")
  private long refreshExpiration;

  @Value("${spring.application.security.jwt.refresh-token.expirationRemember}")
  private long refreshExpirationRemember;

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  public String generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
  }

  public String generateToken(
    Map<String, Object> extraClaims,
    UserDetails userDetails
  ) {
    return buildToken(extraClaims, userDetails, jwtExpiration);
  }

  public String generateRefreshToken(UserDetails userDetails ) {
    return buildToken(new HashMap<>(), userDetails, refreshExpiration);
  }

  public String generateRefreshToken(UserDetails userDetails, boolean remember) {
    return buildToken(new HashMap<>(), userDetails, remember ? refreshExpirationRemember : refreshExpiration);
  }

  private String buildToken(
    Map<String, Object> extraClaims,
    UserDetails userDetails,
    long expiration
  ) {
    return Jwts
      .builder()
      .claims(extraClaims)
      .subject(userDetails.getUsername())
      .issuedAt(new Date(System.currentTimeMillis()))
      .expiration(new Date(System.currentTimeMillis() + expiration))
      .signWith(getSignInKey())
      .compact();
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private Claims extractAllClaims(String token) {
    return Jwts
      .parser()
      .verifyWith(getSignInKey())
      .build()
      .parseSignedClaims(token)
      .getPayload();
  }

  private SecretKey getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public Date getRefreshExpirationToDate(boolean remember) {
    return new Date(System.currentTimeMillis() + (remember ? refreshExpirationRemember : refreshExpiration));
  }

  public LocalDateTime getRefreshExpirationToLocalDate(boolean remember) {
    return LocalDateTime.ofInstant(getRefreshExpirationToDate(remember).toInstant(), ZoneId.systemDefault());
  }

  public Cookie generateTokenCookie(String token) {
    return new Cookie("token", token) {{
      setHttpOnly(true);
      setPath("/");
      setMaxAge((int)jwtExpiration);
    }};
  }

  public Cookie generateRefreshTokenCookie(String refreshToken, boolean remember) {
    return new Cookie("refreshToken", refreshToken) {{
      setHttpOnly(true);
      setPath("/");
      setMaxAge((int)(remember ? refreshExpirationRemember : refreshExpiration));
    }};
  }
}
