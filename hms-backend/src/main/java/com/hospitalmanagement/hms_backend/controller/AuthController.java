package com.hospitalmanagement.hms_backend.controller;

import com.hospitalmanagement.hms_backend.dto.AuthDTO;
import com.hospitalmanagement.hms_backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // Build Login REST API
    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<AuthDTO.JWTAuthResponse> login(@Valid @RequestBody AuthDTO.LoginRequest loginRequest) {
        AuthDTO.JWTAuthResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    // Build Register REST API
    @PostMapping(value = {"/register", "/signup"})
    public ResponseEntity<String> register(@Valid @RequestBody AuthDTO.RegisterRequest registerRequest) {
        String response = authService.register(registerRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Optional: Specific endpoint for patient self-registration
    @PostMapping("/register/patient")
    public ResponseEntity<String> registerPatient(@Valid @RequestBody AuthDTO.RegisterRequest registerRequest) {
        // Here, ensure you only pass necessary fields for patient user creation
        // The service method registerPatientUser could enforce ROLE_PATIENT
        String response = authService.registerPatientUser(registerRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}