package com.hospitalmanagement.hms_backend.service;

import com.hospitalmanagement.hms_backend.dto.AppointmentDTO;
import com.hospitalmanagement.hms_backend.enums.AppointmentStatus;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {
    AppointmentDTO createAppointment(AppointmentDTO appointmentDTO);
    AppointmentDTO getAppointmentById(Long appointmentId);
    List<AppointmentDTO> getAllAppointments();
    List<AppointmentDTO> getAppointmentsByPatientId(Long patientId);
    List<AppointmentDTO> getAppointmentsByDoctorId(Long doctorId);
    List<AppointmentDTO> getAppointmentsForDoctorByDate(Long doctorId, LocalDate date);
    List<AppointmentDTO> getAppointmentsForPatientByDate(Long patientId, LocalDate date);
    AppointmentDTO updateAppointmentStatus(Long appointmentId, AppointmentStatus status);
    AppointmentDTO rescheduleAppointment(Long appointmentId, AppointmentDTO appointmentDTO);
    void cancelAppointment(Long appointmentId);
}