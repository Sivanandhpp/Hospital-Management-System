package com.hospitalmanagement.hms_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vitals") // Matches the SQL table name
public class VitalSign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vitals_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encounter_id", nullable = false)
    private Encounter encounter;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt = LocalDateTime.now();

    @Column(name = "temperature_celsius", precision = 4, scale = 1)
    private BigDecimal temperatureCelsius;

    @Column(name = "blood_pressure_systolic")
    private Integer bloodPressureSystolic;

    @Column(name = "blood_pressure_diastolic")
    private Integer bloodPressureDiastolic;

    @Column(name = "heart_rate_bpm")
    private Integer heartRateBpm;

    @Column(name = "respiratory_rate_rpm")
    private Integer respiratoryRateRpm;

    @Column(name = "spo2_percentage")
    private Integer spo2Percentage;

    @Column(name = "height_cm", precision = 5, scale = 1)
    private BigDecimal heightCm;

    @Column(name = "weight_kg", precision = 5, scale = 1)
    private BigDecimal weightKg;

    @Column(precision = 4, scale = 1)
    private BigDecimal bmi; // Body Mass Index

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // Different from recordedAt, which is when vitals were taken
}