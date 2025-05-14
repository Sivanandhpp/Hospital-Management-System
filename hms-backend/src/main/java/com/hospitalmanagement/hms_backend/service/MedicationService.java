package com.hospitalmanagement.hms_backend.service;

import com.hospitalmanagement.hms_backend.dto.MedicationDTO;
import java.util.List;

public interface MedicationService {
    MedicationDTO addMedication(MedicationDTO medicationDTO);
    MedicationDTO getMedicationById(Long medicationId);
    List<MedicationDTO> getAllMedications();
    List<MedicationDTO> searchMedications(String name);
    MedicationDTO updateMedication(Long medicationId, MedicationDTO medicationDTO);
    void deleteMedication(Long medicationId);
}