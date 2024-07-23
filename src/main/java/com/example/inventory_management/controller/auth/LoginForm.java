package com.example.inventory_management.controller.auth;

import com.example.inventory_management.core.controller.CoreForm;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginForm extends CoreForm {
  private String email;

  private String password;

  private String functionId;

  private String eventId;
}
