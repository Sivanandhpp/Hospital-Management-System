package com.hospitalmanagement.hms_backend.service;

import com.hospitalmanagement.hms_backend.dto.DiagnosisDTO;
import java.util.List;

public interface DiagnosisService {
    DiagnosisDTO addDiagnosis(DiagnosisDTO diagnosisDTO);
    DiagnosisDTO getDiagnosisById(Long diagnosisId);
    List<DiagnosisDTO> getDiagnosesByEncounterId(Long encounterId);
    List<DiagnosisDTO> getDiagnosesByPatientId(Long patientId); // Across all encounters
    DiagnosisDTO updateDiagnosis(Long diagnosisId, DiagnosisDTO diagnosisDTO);
    // Delete might be restricted
}