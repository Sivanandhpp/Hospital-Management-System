package com.hospitalmanagement.hms_backend.service;

import com.hospitalmanagement.hms_backend.dto.EncounterDTO;
import java.util.List;

public interface EncounterService {
    EncounterDTO createEncounter(EncounterDTO encounterDTO);
    EncounterDTO getEncounterById(Long encounterId);
    List<EncounterDTO> getEncountersByPatientId(Long patientId);
    List<EncounterDTO> getEncountersByDoctorId(Long doctorId);
    EncounterDTO updateEncounter(Long encounterId, EncounterDTO encounterDTO);
    // Deleting encounters might be restricted or have cascading effects
}