package com.hospitalmanagement.hms_backend.service;

import com.hospitalmanagement.hms_backend.dto.MedicalHistoryDTO;
import java.util.List;

public interface MedicalHistoryService {
    MedicalHistoryDTO addMedicalHistory(MedicalHistoryDTO medicalHistoryDTO);
    MedicalHistoryDTO getMedicalHistoryById(Long historyId);
    List<MedicalHistoryDTO> getMedicalHistoryByPatientId(Long patientId);
    List<MedicalHistoryDTO> getMedicalHistoryByPatientIdAndType(Long patientId, String historyType);
    MedicalHistoryDTO updateMedicalHistory(Long historyId, MedicalHistoryDTO medicalHistoryDTO);
    void deleteMedicalHistory(Long historyId);
}