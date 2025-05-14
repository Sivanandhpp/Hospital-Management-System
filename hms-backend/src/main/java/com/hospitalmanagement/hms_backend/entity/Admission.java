package com.hospitalmanagement.hms_backend.entity;

import com.hospitalmanagement.hms_backend.enums.AdmissionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "admissions")
public class Admission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admission_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encounter_id")
    private Encounter encounter; // Initial encounter leading to admission

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admitting_doctor_id", nullable = false)
    private Doctor admittingDoctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ward_id", nullable = false)
    private Ward ward;

    @OneToOne(fetch = FetchType.LAZY) // Assuming a bed is unique per admission for active admissions
    @JoinColumn(name = "bed_id", nullable = false, unique = true) // Check if unique is needed if historical data can have same bed
    private Bed bed;

    @Column(name = "admission_datetime", nullable = false)
    private LocalDateTime admissionDatetime;

    @Column(name = "discharge_datetime")
    private LocalDateTime dischargeDatetime;

    @Column(name = "reason_for_admission", columnDefinition = "TEXT")
    private String reasonForAdmission;

    @Column(name = "discharge_summary", columnDefinition = "TEXT")
    private String dischargeSummary;

    @Enumerated(EnumType.STRING)
    @Column(length = 20) // Max length of ENUM values
    private AdmissionStatus status = AdmissionStatus.ADMITTED;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}