package com.hospitalmanagement.hms_backend.dto;

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
public class ConsultationNoteDTO {
    private Long id;

    @NotNull(message = "Encounter ID is required")
    private Long encounterId;

    private String symptoms;
    private String observations;
    private String assessment;
    private String treatmentPlan;
    private String followUpInstructions;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}