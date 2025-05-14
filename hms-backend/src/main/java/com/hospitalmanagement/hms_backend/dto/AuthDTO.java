package com.hospitalmanagement.hms_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

public class AuthDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequest {
        @NotEmpty(message = "Username or email cannot be empty")
        private String usernameOrEmail;

        @NotEmpty(message = "Password cannot be empty")
        private String password;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JWTAuthResponse {
        private String accessToken;
        private String tokenType = "Bearer";
        private String username;
        private Set<String> roles;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterRequest {
        @NotEmpty
        @Size(min = 3, max = 50, message = "Full name must be between 3 and 50 characters")
        private String fullName;

        @NotEmpty
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        private String username;

        @NotEmpty
        @Email(message = "Email should be valid")
        @Size(max = 100)
        private String email;

        @NotEmpty
        @Size(min = 6, message = "Password must be at least 6 characters")
        private String password;

        private Set<String> roles; // e.g., ["ROLE_DOCTOR", "ROLE_RECEPTIONIST"]
    }
}