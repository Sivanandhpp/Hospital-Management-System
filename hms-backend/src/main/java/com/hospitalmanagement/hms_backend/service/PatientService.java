package com.hospitalmanagement.hms_backend.service;

import com.hospitalmanagement.hms_backend.dto.PatientDTO;

import java.util.List;

public interface PatientService {
    PatientDTO createPatient(PatientDTO patientDTO);
    PatientDTO getPatientById(Long patientId);
    List<PatientDTO> getAllPatients();
    PatientDTO updatePatient(Long patientId, PatientDTO patientDTO);
    void deletePatient(Long patientId);
    List<PatientDTO> searchPatients(String query); // Optional: for searching by name, etc.
}