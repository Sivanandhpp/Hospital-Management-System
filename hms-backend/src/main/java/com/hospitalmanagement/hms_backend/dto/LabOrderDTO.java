package com.hospitalmanagement.hms_backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LabOrderDTO {
    private Long id;

    @NotNull(message = "Encounter ID is required")
    private Long encounterId;

    private LocalDateTime orderDatetime;
    private String status;
    private String notes;

    @Valid
    @NotEmpty(message = "Lab order must have at least one test item.")
    private List<LabOrderItemDTO> items;

    private LocalDateTime createdAt;
}