package com.hospitalmanagement.hms_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BedDTO {
    private Long id;

    @NotNull(message = "Ward ID is required")
    private Integer wardId;
    private String wardName; // For display

    @NotBlank(message = "Bed number is required")
    @Size(max = 20, message = "Bed number must be less than 20 characters")
    private String bedNumber;

    private boolean isOccupied;

    @Size(max = 50, message = "Bed type must be less than 50 characters")
    private String bedType;

    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}