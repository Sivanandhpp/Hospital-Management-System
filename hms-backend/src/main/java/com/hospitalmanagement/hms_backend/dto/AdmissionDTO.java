package com.hospitalmanagement.hms_backend.dto;

import com.hospitalmanagement.hms_backend.enums.AdmissionStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionDTO {
    private Long id;

    @NotNull(message = "Patient ID is required")
    private Long patientId;
    private String patientFullName; // For display

    private Long encounterId; // Optional: Encounter leading to admission

    @NotNull(message = "Admitting Doctor ID is required")
    private Long admittingDoctorId;
    private String admittingDoctorFullName; // For display

    @NotNull(message = "Ward ID is required")
    private Integer wardId;
    private String wardName; // For display

    @NotNull(message = "Bed ID is required")
    private Long bedId;
    private String bedNumber; // For display

    @NotNull(message = "Admission date and time are required")
    private LocalDateTime admissionDatetime;

    private LocalDateTime dischargeDatetime;
    private String reasonForAdmission;
    private String dischargeSummary;
    private AdmissionStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}