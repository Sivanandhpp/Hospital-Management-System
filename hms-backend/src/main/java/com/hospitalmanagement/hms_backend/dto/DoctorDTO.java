package com.hospitalmanagement.hms_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO {
    private Long id; // Doctor's specific ID from doctors table

    @NotNull(message = "User ID for the doctor is required")
    private Long userId; // Corresponds to the ID in the users table

    private String username; // For display, fetched from User entity
    private String fullName; // For display, fetched from User entity
    private String email;    // For display, fetched from User entity

    @NotBlank(message = "Specialization is required")
    private String specialization;

    private String licenseNumber;
    private String phoneNumber; // Doctor's specific contact
}