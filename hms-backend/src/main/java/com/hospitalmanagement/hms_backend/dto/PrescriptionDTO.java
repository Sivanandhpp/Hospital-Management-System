package com.hospitalmanagement.hms_backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionDTO {
    private Long id;

    @NotNull(message = "Encounter ID is required")
    private Long encounterId;

    @NotNull(message = "Prescription date is required")
    private LocalDate prescriptionDate;

    private String notes;

    @Valid // Enable validation for items in the list
    @NotEmpty(message = "Prescription must have at least one item.")
    private List<PrescriptionItemDTO> items;

    private LocalDateTime createdAt;
}