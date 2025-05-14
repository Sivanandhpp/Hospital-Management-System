package com.hospitalmanagement.hms_backend.service;

import com.hospitalmanagement.hms_backend.dto.AuthDTO;

public interface AuthService {
    AuthDTO.JWTAuthResponse login(AuthDTO.LoginRequest loginRequest);
    String register(AuthDTO.RegisterRequest registerRequest);
    String registerPatientUser(AuthDTO.RegisterRequest registerRequest); // Specific for patient self-registration if needed
}