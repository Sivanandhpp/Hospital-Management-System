package com.hospitalmanagement.hms_backend.service;

import com.hospitalmanagement.hms_backend.dto.PrescriptionDTO;
import java.util.List;

public interface PrescriptionService {
    PrescriptionDTO createPrescription(PrescriptionDTO prescriptionDTO);
    PrescriptionDTO getPrescriptionById(Long prescriptionId);
    PrescriptionDTO getPrescriptionByEncounterId(Long encounterId);
    List<PrescriptionDTO> getPrescriptionsByPatientId(Long patientId);
    // Update might be complex: add/remove items or update existing.
    // Deletion needs careful consideration.
}