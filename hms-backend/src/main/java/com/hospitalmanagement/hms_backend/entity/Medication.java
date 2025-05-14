package com.hospitalmanagement.hms_backend.entity;

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
@Table(name = "medications")
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medication_id")
    private Long id;

    @Column(name = "medication_name", nullable = false, unique = true)
    private String medicationName;

    @Column(name = "generic_name")
    private String genericName;

    @Column(length = 100)
    private String manufacturer;

    @Column(length = 50)
    private String strength; // e.g., 500mg, 10ml

    @Column(length = 50)
    private String form; // e.g., Tablet, Syrup, Injection

    @Column(columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}