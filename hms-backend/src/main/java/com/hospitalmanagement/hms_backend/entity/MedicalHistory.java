package com.hospitalmanagement.hms_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "medical_history")
public class MedicalHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    // Using String for flexibility, could be an ENUM if types are strictly defined
    @Column(name = "history_type", nullable = false, length = 50)
    private String historyType; // e.g., ALLERGY, PAST_ILLNESS, SURGERY, FAMILY_HISTORY, SOCIAL_HISTORY, IMMUNIZATION

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "recorded_date")
    private LocalDate recordedDate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}