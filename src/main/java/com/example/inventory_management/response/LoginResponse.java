package com.example.inventory_management.response;

import com.example.inventory_management.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

  private UserDto user;

  private String token;

  private String refreshToken; 
}
