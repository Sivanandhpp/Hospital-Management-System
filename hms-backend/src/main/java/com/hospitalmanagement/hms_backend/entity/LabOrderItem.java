package com.hospitalmanagement.hms_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lab_order_items")
public class LabOrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lab_order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_order_id", nullable = false)
    private LabOrder labOrder;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lab_test_id", nullable = false)
    private LabTest labTest;

    @Column(name = "result_value")
    private String resultValue;

    @Column(name = "result_unit", length = 50)
    private String resultUnit;

    @Column(name = "result_datetime")
    private LocalDateTime resultDatetime;

    // CORRECTED FIELD NAME HERE
    @Column(name = "is_abnormal") // Database column can remain is_abnormal
    private Boolean abnormal; // Field name changed to 'abnormal'

    @Column(name = "technician_notes", columnDefinition = "TEXT")
    private String technicianNotes;

    @Column(name = "result_attachment_path")
    private String resultAttachmentPath;
}