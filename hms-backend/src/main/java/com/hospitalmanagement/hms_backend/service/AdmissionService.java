package com.hospitalmanagement.hms_backend.service;

import com.hospitalmanagement.hms_backend.dto.AdmissionDTO;
import com.hospitalmanagement.hms_backend.dto.DischargeRequestDTO;
import java.util.List;

public interface AdmissionService {
    AdmissionDTO admitPatient(AdmissionDTO admissionDTO);
    AdmissionDTO getAdmissionById(Long admissionId);
    AdmissionDTO getActiveAdmissionByPatientId(Long patientId);
    List<AdmissionDTO> getAllAdmissions();
    List<AdmissionDTO> getAdmissionsByPatientId(Long patientId);
    List<AdmissionDTO> getAdmissionsByWardId(Integer wardId);
    AdmissionDTO dischargePatient(Long admissionId, DischargeRequestDTO dischargeRequestDTO);
    // Transfer patient might involve creating a new admission record and updating the old one.
    // AdmissionDTO transferPatient(Long currentAdmissionId, Integer newWardId, Long newBedId, String transferReason);
    // For simplicity, transfer is not fully implemented here but would be an extension.
}