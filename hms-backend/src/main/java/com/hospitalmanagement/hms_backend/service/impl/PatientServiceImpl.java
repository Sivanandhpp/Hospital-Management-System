package com.hospitalmanagement.hms_backend.service.impl;

import com.hospitalmanagement.hms_backend.dto.PatientDTO;
import com.hospitalmanagement.hms_backend.entity.Patient;
import com.hospitalmanagement.hms_backend.exception.ResourceNotFoundException;
import com.hospitalmanagement.hms_backend.repository.PatientRepository;
import com.hospitalmanagement.hms_backend.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public PatientDTO createPatient(PatientDTO patientDTO) {
        // Check if email already exists for another patient
        patientRepository.findByEmail(patientDTO.getEmail()).ifPresent(p -> {
            throw new IllegalArgumentException("Email " + patientDTO.getEmail() + " is already registered.");
        });

        Patient patient = modelMapper.map(patientDTO, Patient.class);
        Patient savedPatient = patientRepository.save(patient);
        return modelMapper.map(savedPatient, PatientDTO.class);
    }

    @Override
    public PatientDTO getPatientById(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", patientId));
        return modelMapper.map(patient, PatientDTO.class);
    }

    @Override
    public List<PatientDTO> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream()
                .map(patient -> modelMapper.map(patient, PatientDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PatientDTO updatePatient(Long patientId, PatientDTO patientDTO) {
        Patient existingPatient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", patientId));

        // Check if the updated email is being used by another patient
        if (patientDTO.getEmail() != null && !patientDTO.getEmail().equals(existingPatient.getEmail())) {
            patientRepository.findByEmail(patientDTO.getEmail()).ifPresent(p -> {
                if (!p.getId().equals(patientId)) {
                    throw new IllegalArgumentException("Email " + patientDTO.getEmail() + " is already registered by another patient.");
                }
            });
        }

        // Use ModelMapper to map non-null DTO fields to the entity
        // Be careful with ModelMapper's default behavior for nulls; you might need custom configuration
        // For simplicity, we're mapping all fields. Consider partial updates carefully.
        modelMapper.map(patientDTO, existingPatient);
        existingPatient.setId(patientId); // Ensure ID is not changed

        Patient updatedPatient = patientRepository.save(existingPatient);
        return modelMapper.map(updatedPatient, PatientDTO.class);
    }

    @Override
    @Transactional
    public void deletePatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", patientId));
        // Add checks for related data (appointments, admissions) if cascading delete is not desired or if you need custom logic
        patientRepository.delete(patient);
    }

    @Override
    public List<PatientDTO> searchPatients(String query) {
        // This is a very basic search. For production, consider more robust search solutions.
        // For example, using Spring Data JPA query methods or Specifications.
        // Example: findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase
        // For simplicity, filtering in memory here (not efficient for large datasets).
        List<Patient> patients = patientRepository.findAll();
        String lowerCaseQuery = query.toLowerCase();
        return patients.stream()
                .filter(p -> (p.getFirstName() != null && p.getFirstName().toLowerCase().contains(lowerCaseQuery)) ||
                             (p.getLastName() != null && p.getLastName().toLowerCase().contains(lowerCaseQuery)) ||
                             (p.getEmail() != null && p.getEmail().toLowerCase().contains(lowerCaseQuery)) ||
                             (p.getPhoneNumber() != null && p.getPhoneNumber().contains(query))) // Phone might not need to be lowercased
                .map(patient -> modelMapper.map(patient, PatientDTO.class))
                .collect(Collectors.toList());
    }
}