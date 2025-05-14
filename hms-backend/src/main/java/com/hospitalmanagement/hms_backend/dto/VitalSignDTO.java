package com.hospitalmanagement.hms_backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VitalSignDTO {
    private Long id;

    @NotNull(message = "Encounter ID is required")
    private Long encounterId;

    private LocalDateTime recordedAt;
    private BigDecimal temperatureCelsius;
    private Integer bloodPressureSystolic;
    private Integer bloodPressureDiastolic;
    private Integer heartRateBpm;
    private Integer respiratoryRateRpm;
    private Integer spo2Percentage;
    private BigDecimal heightCm;
    private BigDecimal weightKg;
    private BigDecimal bmi;
    private String notes;
    private LocalDateTime createdAt;
}