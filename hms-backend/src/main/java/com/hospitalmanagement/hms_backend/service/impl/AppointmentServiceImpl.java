package com.hospitalmanagement.hms_backend.service.impl;

import com.hospitalmanagement.hms_backend.dto.AppointmentDTO;
import com.hospitalmanagement.hms_backend.entity.Appointment;
import com.hospitalmanagement.hms_backend.entity.Doctor;
import com.hospitalmanagement.hms_backend.entity.Patient;
import com.hospitalmanagement.hms_backend.enums.AppointmentStatus;
import com.hospitalmanagement.hms_backend.exception.HospitalAPIException;
import com.hospitalmanagement.hms_backend.exception.ResourceNotFoundException;
import com.hospitalmanagement.hms_backend.repository.AppointmentRepository;
import com.hospitalmanagement.hms_backend.repository.DoctorRepository;
import com.hospitalmanagement.hms_backend.repository.PatientRepository;
import com.hospitalmanagement.hms_backend.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public AppointmentDTO createAppointment(AppointmentDTO appointmentDTO) {
        Patient patient = patientRepository.findById(appointmentDTO.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", appointmentDTO.getPatientId()));
        Doctor doctor = doctorRepository.findById(appointmentDTO.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", appointmentDTO.getDoctorId()));

        // Basic validation: appointment time in future
        if (appointmentDTO.getAppointmentDatetime().isBefore(LocalDateTime.now())) {
            throw new HospitalAPIException(HttpStatus.BAD_REQUEST, "Appointment time must be in the future.");
        }

        // TODO: Add more complex validation: doctor availability, no overlapping appointments for doctor/patient

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDatetime(appointmentDTO.getAppointmentDatetime());
        appointment.setReasonForVisit(appointmentDTO.getReasonForVisit());
        appointment.setStatus(AppointmentStatus.SCHEDULED); // Default status
        appointment.setNotes(appointmentDTO.getNotes());

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return mapToDTO(savedAppointment);
    }

    @Override
    public AppointmentDTO getAppointmentById(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", appointmentId));
        return mapToDTO(appointment);
    }

    @Override
    public List<AppointmentDTO> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> getAppointmentsByPatientId(Long patientId) {
        patientRepository.findById(patientId) // Check if patient exists
            .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", patientId));
        return appointmentRepository.findByPatientId(patientId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> getAppointmentsByDoctorId(Long doctorId) {
        doctorRepository.findById(doctorId) // Check if doctor exists
            .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", doctorId));
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> getAppointmentsForDoctorByDate(Long doctorId, LocalDate date) {
        doctorRepository.findById(doctorId)
            .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", doctorId));
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return appointmentRepository.findByDoctorIdAndAppointmentDatetimeBetween(doctorId, startOfDay, endOfDay)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> getAppointmentsForPatientByDate(Long patientId, LocalDate date) {
         patientRepository.findById(patientId)
            .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", patientId));
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return appointmentRepository.findByPatientIdAndAppointmentDatetimeBetween(patientId, startOfDay, endOfDay)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public AppointmentDTO updateAppointmentStatus(Long appointmentId, AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", appointmentId));
        appointment.setStatus(status);
        Appointment updatedAppointment = appointmentRepository.save(appointment);
        return mapToDTO(updatedAppointment);
    }

    @Override
    @Transactional
    public AppointmentDTO rescheduleAppointment(Long appointmentId, AppointmentDTO appointmentDTO) {
        Appointment existingAppointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", appointmentId));

        if (appointmentDTO.getAppointmentDatetime() == null || appointmentDTO.getAppointmentDatetime().isBefore(LocalDateTime.now())) {
            throw new HospitalAPIException(HttpStatus.BAD_REQUEST, "New appointment time must be in the future.");
        }

        // Update fields if provided
        if (appointmentDTO.getAppointmentDatetime() != null) {
            existingAppointment.setAppointmentDatetime(appointmentDTO.getAppointmentDatetime());
        }
        if (appointmentDTO.getReasonForVisit() != null) {
            existingAppointment.setReasonForVisit(appointmentDTO.getReasonForVisit());
        }
        if (appointmentDTO.getNotes() != null) {
            existingAppointment.setNotes(appointmentDTO.getNotes());
        }
        existingAppointment.setStatus(AppointmentStatus.RESCHEDULED); // Or keep original if needed

        // Re-validate doctor and patient if IDs are changed in DTO (usually not done for reschedule)
        // For simplicity, assuming doctor and patient remain the same for a reschedule.

        Appointment updatedAppointment = appointmentRepository.save(existingAppointment);
        return mapToDTO(updatedAppointment);
    }

    @Override
    @Transactional
    public void cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", appointmentId));

        // You can choose to actually delete or just mark as CANCELLED
        // Marking as CANCELLED is usually better for audit trails
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
             throw new HospitalAPIException(HttpStatus.BAD_REQUEST, "Cannot cancel a completed appointment.");
        }
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
        // appointmentRepository.delete(appointment); // If you want to hard delete
    }

    private AppointmentDTO mapToDTO(Appointment appointment) {
        AppointmentDTO dto = modelMapper.map(appointment, AppointmentDTO.class);
        if (appointment.getPatient() != null) {
            dto.setPatientId(appointment.getPatient().getId());
            dto.setPatientFirstName(appointment.getPatient().getFirstName());
            dto.setPatientLastName(appointment.getPatient().getLastName());
        }
        if (appointment.getDoctor() != null) {
            dto.setDoctorId(appointment.getDoctor().getId());
            if (appointment.getDoctor().getUser() != null) { // Doctor's name comes from User
                 dto.setDoctorFirstName(appointment.getDoctor().getUser().getFullName().split(" ")[0]); // Basic split
                 dto.setDoctorLastName(appointment.getDoctor().getUser().getFullName().substring(appointment.getDoctor().getUser().getFullName().indexOf(" ") + 1)); // Basic split
            }
        }
        return dto;
    }
}