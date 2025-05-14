package com.hospitalmanagement.hms_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LabOrderResultUpdateDTO {
    @NotBlank(message = "Result value is required")
    private String resultValue;

    private String resultUnit;

    @NotNull(message = "Result date and time are required")
    private LocalDateTime resultDatetime;

    // Field name matching the entity's new field name for clarity
    private Boolean abnormal; // Changed from isAbnormal to abnormal

    private String technicianNotes;
    private String resultAttachmentPath;
}