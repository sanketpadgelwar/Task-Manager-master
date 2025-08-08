package com.craft.tmanager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.craft.tmanager.dto.JwtResponse;
import com.craft.tmanager.dto.LoginRequest;

@RestController
public class LoginController {

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(new JwtResponse("jwt-token", "ROLE_ADMIN"));
    }
}
