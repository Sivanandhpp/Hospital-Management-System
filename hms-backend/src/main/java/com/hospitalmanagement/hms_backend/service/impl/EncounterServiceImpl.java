package com.hospitalmanagement.hms_backend.service.impl;

import com.hospitalmanagement.hms_backend.dto.EncounterDTO;
import com.hospitalmanagement.hms_backend.entity.Appointment;
import com.hospitalmanagement.hms_backend.entity.Doctor;
import com.hospitalmanagement.hms_backend.entity.Encounter;
import com.hospitalmanagement.hms_backend.entity.Patient;
import com.hospitalmanagement.hms_backend.exception.ResourceNotFoundException;
import com.hospitalmanagement.hms_backend.repository.AppointmentRepository;
import com.hospitalmanagement.hms_backend.repository.DoctorRepository;
import com.hospitalmanagement.hms_backend.repository.EncounterRepository;
import com.hospitalmanagement.hms_backend.repository.PatientRepository;
import com.hospitalmanagement.hms_backend.service.EncounterService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EncounterServiceImpl implements EncounterService {

    private final EncounterRepository encounterRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository; // Optional
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public EncounterDTO createEncounter(EncounterDTO encounterDTO) {
        Patient patient = patientRepository.findById(encounterDTO.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", encounterDTO.getPatientId()));
        Doctor doctor = doctorRepository.findById(encounterDTO.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", encounterDTO.getDoctorId()));

        Encounter encounter = new Encounter();
        encounter.setPatient(patient);
        encounter.setDoctor(doctor);
        encounter.setEncounterDatetime(encounterDTO.getEncounterDatetime());
        encounter.setEncounterType(encounterDTO.getEncounterType());
        encounter.setChiefComplaint(encounterDTO.getChiefComplaint());

        if (encounterDTO.getAppointmentId() != null) {
            Appointment appointment = appointmentRepository.findById(encounterDTO.getAppointmentId()).orElse(null);
            encounter.setAppointment(appointment);
        }

        Encounter savedEncounter = encounterRepository.save(encounter);
        return mapToDTO(savedEncounter);
    }

    @Override
    public EncounterDTO getEncounterById(Long encounterId) {
        Encounter encounter = encounterRepository.findById(encounterId)
                .orElseThrow(() -> new ResourceNotFoundException("Encounter", "id", encounterId));
        return mapToDTO(encounter);
    }

    @Override
    public List<EncounterDTO> getEncountersByPatientId(Long patientId) {
        patientRepository.findById(patientId).orElseThrow(() -> new ResourceNotFoundException("Patient", "id", patientId));
        return encounterRepository.findByPatientId(patientId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EncounterDTO> getEncountersByDoctorId(Long doctorId) {
        doctorRepository.findById(doctorId).orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", doctorId));
        return encounterRepository.findByDoctorId(doctorId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EncounterDTO updateEncounter(Long encounterId, EncounterDTO encounterDTO) {
        Encounter existingEncounter = encounterRepository.findById(encounterId)
                .orElseThrow(() -> new ResourceNotFoundException("Encounter", "id", encounterId));

        // Update relevant fields, patient/doctor usually don't change for an existing encounter
        if(encounterDTO.getEncounterDatetime() != null) existingEncounter.setEncounterDatetime(encounterDTO.getEncounterDatetime());
        if(encounterDTO.getEncounterType() != null) existingEncounter.setEncounterType(encounterDTO.getEncounterType());
        if(encounterDTO.getChiefComplaint() != null) existingEncounter.setChiefComplaint(encounterDTO.getChiefComplaint());
        
        Encounter updatedEncounter = encounterRepository.save(existingEncounter);
        return mapToDTO(updatedEncounter);
    }

    private EncounterDTO mapToDTO(Encounter encounter) {
        EncounterDTO dto = modelMapper.map(encounter, EncounterDTO.class);
        if (encounter.getPatient() != null) {
            dto.setPatientId(encounter.getPatient().getId());
            dto.setPatientFullName(encounter.getPatient().getFirstName() + " " + encounter.getPatient().getLastName());
        }
        if (encounter.getDoctor() != null && encounter.getDoctor().getUser() != null) {
            dto.setDoctorId(encounter.getDoctor().getId());
            dto.setDoctorFullName(encounter.getDoctor().getUser().getFullName());
        }
        if (encounter.getAppointment() != null) {
            dto.setAppointmentId(encounter.getAppointment().getId());
        }
        return dto;
    }
}