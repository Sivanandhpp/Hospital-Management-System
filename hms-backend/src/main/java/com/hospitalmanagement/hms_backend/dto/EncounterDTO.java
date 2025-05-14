package com.hospitalmanagement.hms_backend.dto;

import jakarta.validation.constraints.NotBlank;
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
public class EncounterDTO {
    private Long id;

    @NotNull(message = "Patient ID is required")
    private Long patientId;
    private String patientFullName;

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;
    private String doctorFullName;

    private Long appointmentId; // Optional

    @NotNull(message = "Encounter date and time are required")
    private LocalDateTime encounterDatetime;

    @NotBlank(message = "Encounter type is required")
    private String encounterType; // e.g., CONSULTATION, FOLLOW_UP

    private String chiefComplaint;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}