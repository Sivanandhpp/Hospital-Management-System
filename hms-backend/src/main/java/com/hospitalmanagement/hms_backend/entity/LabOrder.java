package com.hospitalmanagement.hms_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lab_orders")
public class LabOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lab_order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encounter_id", nullable = false)
    private Encounter encounter;

    @Column(name = "order_datetime", nullable = false)
    private LocalDateTime orderDatetime = LocalDateTime.now();

    // Using String for status, ENUM could be better if statuses are fixed
    @Column(length = 30)
    private String status = "ORDERED"; // e.g., ORDERED, SAMPLE_COLLECTED, COMPLETED

    @Column(columnDefinition = "TEXT")
    private String notes; // Notes for the overall lab order

    @OneToMany(mappedBy = "labOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<LabOrderItem> items = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public void addItem(LabOrderItem item) {
        items.add(item);
        item.setLabOrder(this);
    }

    public void removeItem(LabOrderItem item) {
        items.remove(item);
        item.setLabOrder(null);
    }
}