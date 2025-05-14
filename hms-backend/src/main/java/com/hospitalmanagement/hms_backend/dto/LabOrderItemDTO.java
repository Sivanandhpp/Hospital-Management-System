package com.hospitalmanagement.hms_backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LabOrderItemDTO {
    private Long id;

    @NotNull(message = "Lab Test ID is required")
    private Long labTestId;
    private String labTestName;

    private String resultValue;
    private String resultUnit;
    private LocalDateTime resultDatetime;
    private Boolean abnormal; // Changed from isAbnormal
    private String technicianNotes;
    private String resultAttachmentPath;
}