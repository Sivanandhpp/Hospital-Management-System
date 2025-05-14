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
public class LabTestDTO {
    private Long id;
    @NotBlank(message = "Test name is required")
    private String testName;
    private String description;
    private String category;
    private String normalRange;
    private String unit;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}