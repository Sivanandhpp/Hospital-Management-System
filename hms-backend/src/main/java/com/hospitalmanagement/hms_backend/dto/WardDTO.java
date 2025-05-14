package com.hospitalmanagement.hms_backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
public class WardDTO {
    private Integer id;

    @NotBlank(message = "Ward name is required")
    @Size(max = 100, message = "Ward name must be less than 100 characters")
    private String wardName;

    @Size(max = 50, message = "Ward type must be less than 50 characters")
    private String wardType;

    @Size(max = 255, message = "Location description must be less than 255 characters")
    private String locationDescription;

    @Min(value = 0, message = "Capacity must be a non-negative number")
    private Integer capacity;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}