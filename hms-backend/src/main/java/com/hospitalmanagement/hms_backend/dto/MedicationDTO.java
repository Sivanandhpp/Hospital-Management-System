package com.hospitalmanagement.hms_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicationDTO {
    private Long id;
    @NotBlank(message = "Medication name is required")
    private String medicationName;
    private String genericName;
    private String manufacturer;
    private String strength;
    private String form;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}