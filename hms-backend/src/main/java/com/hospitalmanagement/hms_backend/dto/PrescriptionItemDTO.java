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
public class PrescriptionItemDTO {
    private Long id; // prescription_item_id

    @NotNull(message = "Medication ID is required")
    private Long medicationId;
    private String medicationName; // For display

    @NotBlank(message = "Dosage is required")
    private String dosage;

    @NotBlank(message = "Frequency is required")
    private String frequency;

    private String duration;
    private String route;
    private String instructions;
}