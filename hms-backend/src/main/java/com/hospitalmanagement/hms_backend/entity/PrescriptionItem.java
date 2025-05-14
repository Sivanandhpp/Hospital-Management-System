package com.hospitalmanagement.hms_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "prescription_items")
public class PrescriptionItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prescription_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id", nullable = false)
    private Prescription prescription;

    @ManyToOne(fetch = FetchType.EAGER) // EAGER to load medication details with item
    @JoinColumn(name = "medication_id", nullable = false)
    private Medication medication;

    @Column(nullable = false, length = 100)
    private String dosage; // e.g., 1 tablet, 5ml

    @Column(nullable = false, length = 100)
    private String frequency; // e.g., Twice a day, Every 4 hours

    @Column(length = 100)
    private String duration; // e.g., 7 days, As needed

    @Column(length = 50)
    private String route; // e.g., Oral, IV, Topical

    @Column(columnDefinition = "TEXT")
    private String instructions;
}