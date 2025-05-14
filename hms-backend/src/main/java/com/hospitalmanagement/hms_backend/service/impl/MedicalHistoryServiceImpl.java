package com.hospitalmanagement.hms_backend.service.impl;

import com.hospitalmanagement.hms_backend.dto.MedicalHistoryDTO;
import com.hospitalmanagement.hms_backend.entity.MedicalHistory;
import com.hospitalmanagement.hms_backend.entity.Patient;
import com.hospitalmanagement.hms_backend.exception.ResourceNotFoundException;
import com.hospitalmanagement.hms_backend.repository.MedicalHistoryRepository;
import com.hospitalmanagement.hms_backend.repository.PatientRepository;
import com.hospitalmanagement.hms_backend.service.MedicalHistoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicalHistoryServiceImpl implements MedicalHistoryService {

    private final MedicalHistoryRepository medicalHistoryRepository;
    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public MedicalHistoryDTO addMedicalHistory(MedicalHistoryDTO dto) {
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", dto.getPatientId()));
        MedicalHistory medicalHistory = modelMapper.map(dto, MedicalHistory.class);
        medicalHistory.setPatient(patient);
        MedicalHistory savedHistory = medicalHistoryRepository.save(medicalHistory);
        return modelMapper.map(savedHistory, MedicalHistoryDTO.class);
    }

    @Override
    public MedicalHistoryDTO getMedicalHistoryById(Long historyId) {
        MedicalHistory history = medicalHistoryRepository.findById(historyId)
                .orElseThrow(() -> new ResourceNotFoundException("MedicalHistory", "id", historyId));
        return modelMapper.map(history, MedicalHistoryDTO.class);
    }

    @Override
    public List<MedicalHistoryDTO> getMedicalHistoryByPatientId(Long patientId) {
        patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", patientId));
        return medicalHistoryRepository.findByPatientId(patientId).stream()
                .map(history -> modelMapper.map(history, MedicalHistoryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicalHistoryDTO> getMedicalHistoryByPatientIdAndType(Long patientId, String historyType) {
        patientRepository.findById(patientId)
            .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", patientId));
        return medicalHistoryRepository.findByPatientIdAndHistoryType(patientId, historyType).stream()
                .map(history -> modelMapper.map(history, MedicalHistoryDTO.class))
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public MedicalHistoryDTO updateMedicalHistory(Long historyId, MedicalHistoryDTO dto) {
        MedicalHistory existingHistory = medicalHistoryRepository.findById(historyId)
                .orElseThrow(() -> new ResourceNotFoundException("MedicalHistory", "id", historyId));
        
        // Ensure patient doesn't change
        if (!existingHistory.getPatient().getId().equals(dto.getPatientId())) {
            throw new IllegalArgumentException("Cannot change patient for existing medical history record.");
        }

        modelMapper.map(dto, existingHistory);
        existingHistory.setId(historyId); // ensure ID is preserved

        MedicalHistory updatedHistory = medicalHistoryRepository.save(existingHistory);
        return modelMapper.map(updatedHistory, MedicalHistoryDTO.class);
    }

    @Override
    @Transactional
    public void deleteMedicalHistory(Long historyId) {
        MedicalHistory history = medicalHistoryRepository.findById(historyId)
                .orElseThrow(() -> new ResourceNotFoundException("MedicalHistory", "id", historyId));
        medicalHistoryRepository.delete(history);
    }
}