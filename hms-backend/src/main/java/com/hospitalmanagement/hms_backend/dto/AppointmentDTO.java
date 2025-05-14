package com.hospitalmanagement.hms_backend.dto;

import com.hospitalmanagement.hms_backend.enums.AppointmentStatus;
import jakarta.validation.constraints.FutureOrPresent;
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
public class AppointmentDTO {
    private Long id;

    @NotNull(message = "Patient ID is required")
    private Long patientId;
    private String patientFirstName; // For display
    private String patientLastName;  // For display

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;
    private String doctorFirstName; // For display
    private String doctorLastName;  // For display

    @NotNull(message = "Appointment date and time are required")
    @FutureOrPresent(message = "Appointment date must be in the present or future")
    private LocalDateTime appointmentDatetime;

    private String reasonForVisit;
    private AppointmentStatus status;
    private String notes; // Receptionist/patient notes

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}