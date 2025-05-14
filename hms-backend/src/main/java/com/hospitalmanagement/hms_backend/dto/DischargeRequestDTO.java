package com.hospitalmanagement.hms_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DischargeRequestDTO {
    @NotNull(message = "Discharge date and time are required")
    private LocalDateTime dischargeDatetime;

    @NotBlank(message = "Discharge summary is required")
    private String dischargeSummary;
}