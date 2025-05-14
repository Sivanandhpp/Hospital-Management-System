package com.hospitalmanagement.hms_backend.service.impl;

import com.hospitalmanagement.hms_backend.dto.DiagnosisDTO;
import com.hospitalmanagement.hms_backend.entity.Diagnosis;
import com.hospitalmanagement.hms_backend.entity.Encounter;
import com.hospitalmanagement.hms_backend.exception.ResourceNotFoundException;
import com.hospitalmanagement.hms_backend.repository.DiagnosisRepository;
import com.hospitalmanagement.hms_backend.repository.EncounterRepository;
import com.hospitalmanagement.hms_backend.service.DiagnosisService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiagnosisServiceImpl implements DiagnosisService {

    private final DiagnosisRepository diagnosisRepository;
    private final EncounterRepository encounterRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public DiagnosisDTO addDiagnosis(DiagnosisDTO dto) {
        Encounter encounter = encounterRepository.findById(dto.getEncounterId())
                .orElseThrow(() -> new ResourceNotFoundException("Encounter", "id", dto.getEncounterId()));
        Diagnosis diagnosis = modelMapper.map(dto, Diagnosis.class);
        diagnosis.setEncounter(encounter);
        Diagnosis savedDiagnosis = diagnosisRepository.save(diagnosis);
        return modelMapper.map(savedDiagnosis, DiagnosisDTO.class);
    }

    @Override
    public DiagnosisDTO getDiagnosisById(Long diagnosisId) {
        Diagnosis diagnosis = diagnosisRepository.findById(diagnosisId)
                .orElseThrow(() -> new ResourceNotFoundException("Diagnosis", "id", diagnosisId));
        return modelMapper.map(diagnosis, DiagnosisDTO.class);
    }

    @Override
    public List<DiagnosisDTO> getDiagnosesByEncounterId(Long encounterId) {
        encounterRepository.findById(encounterId)
            .orElseThrow(() -> new ResourceNotFoundException("Encounter", "id", encounterId));
        return diagnosisRepository.findByEncounterId(encounterId).stream()
                .map(diag -> modelMapper.map(diag, DiagnosisDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<DiagnosisDTO> getDiagnosesByPatientId(Long patientId) {
        // Ensure patient exists if needed, or just rely on the query.
        // Patient patient = patientRepository.findById(patientId)
        //        .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", patientId));
        return diagnosisRepository.findByEncounterPatientId(patientId).stream()
            .map(diag -> modelMapper.map(diag, DiagnosisDTO.class))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DiagnosisDTO updateDiagnosis(Long diagnosisId, DiagnosisDTO dto) {
        Diagnosis existingDiagnosis = diagnosisRepository.findById(diagnosisId)
                .orElseThrow(() -> new ResourceNotFoundException("Diagnosis", "id", diagnosisId));
        
        // Ensure encounter doesn't change or handle appropriately
        if (!existingDiagnosis.getEncounter().getId().equals(dto.getEncounterId())) {
            Encounter newEncounter = encounterRepository.findById(dto.getEncounterId())
                .orElseThrow(() -> new ResourceNotFoundException("Encounter", "id", dto.getEncounterId()));
            existingDiagnosis.setEncounter(newEncounter);
        }

        modelMapper.map(dto, existingDiagnosis);
        existingDiagnosis.setId(diagnosisId); // preserve ID

        Diagnosis updatedDiagnosis = diagnosisRepository.save(existingDiagnosis);
        return modelMapper.map(updatedDiagnosis, DiagnosisDTO.class);
    }
}