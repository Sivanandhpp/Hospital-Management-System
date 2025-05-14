package com.hospitalmanagement.hms_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisDTO {
    private Long id;

    @NotNull(message = "Encounter ID is required")
    private Long encounterId;

    private String diagnosisCodeSystem;

    @NotBlank(message = "Diagnosis code is required")
    private String diagnosisCode;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Diagnosis date is required")
    private LocalDate diagnosisDate;

    private boolean isChronic;
    private String notes;
    private LocalDateTime createdAt;
}