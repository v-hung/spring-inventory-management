package com.example.inventory_management.core.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import ua_parser.Client;
import ua_parser.Parser;

@RestController
public class TestController {

  @GetMapping("/ping")
  public String ping() {
    return "pong";
  }

  @GetMapping("/device-info")
  public ResponseEntity<?> deviceInfo(HttpServletRequest request) {
    Parser uaParser = new Parser();
    Client c = uaParser.parse(request.getHeader("user-agent"));

    return ResponseEntity.ok(c);
  }

}
