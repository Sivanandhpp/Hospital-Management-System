package com.hospitalmanagement.hms_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "diagnoses") // Matches the SQL table name
public class Diagnosis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diagnosis_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encounter_id", nullable = false)
    private Encounter encounter;

    @Column(name = "diagnosis_code_system", length = 20)
    private String diagnosisCodeSystem = "ICD-10";

    @Column(name = "diagnosis_code", nullable = false, length = 50)
    private String diagnosisCode;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "diagnosis_date", nullable = false)
    private LocalDate diagnosisDate;

    @Column(name = "is_chronic", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isChronic = false;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}