package com.hospitalmanagement.hms_backend.service.impl;

import com.hospitalmanagement.hms_backend.dto.MedicationDTO;
import com.hospitalmanagement.hms_backend.entity.Medication;
import com.hospitalmanagement.hms_backend.exception.HospitalAPIException;
import com.hospitalmanagement.hms_backend.exception.ResourceNotFoundException;
import com.hospitalmanagement.hms_backend.repository.MedicationRepository;
import com.hospitalmanagement.hms_backend.service.MedicationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicationServiceImpl implements MedicationService {
    private final MedicationRepository medicationRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public MedicationDTO addMedication(MedicationDTO dto) {
        medicationRepository.findByMedicationName(dto.getMedicationName()).ifPresent(m -> {
            throw new HospitalAPIException(HttpStatus.BAD_REQUEST, "Medication with name '" + dto.getMedicationName() + "' already exists.");
        });
        Medication medication = modelMapper.map(dto, Medication.class);
        return modelMapper.map(medicationRepository.save(medication), MedicationDTO.class);
    }
    // Implement other methods: getById, getAll, search, update, delete
    // Similar to other catalog services like WardService or PatientService

    @Override
    public MedicationDTO getMedicationById(Long medicationId) {
        Medication medication = medicationRepository.findById(medicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Medication", "id", medicationId));
        return modelMapper.map(medication, MedicationDTO.class);
    }

    @Override
    public List<MedicationDTO> getAllMedications() {
        return medicationRepository.findAll().stream()
                .map(med -> modelMapper.map(med, MedicationDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicationDTO> searchMedications(String name) {
         return medicationRepository.findByMedicationNameContainingIgnoreCase(name).stream()
                .map(med -> modelMapper.map(med, MedicationDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MedicationDTO updateMedication(Long medicationId, MedicationDTO medicationDTO) {
        Medication existingMedication = medicationRepository.findById(medicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Medication", "id", medicationId));
        
        if (medicationDTO.getMedicationName() != null && !medicationDTO.getMedicationName().equals(existingMedication.getMedicationName())) {
            medicationRepository.findByMedicationName(medicationDTO.getMedicationName()).ifPresent(m -> {
                if (!m.getId().equals(medicationId)) {
                     throw new HospitalAPIException(HttpStatus.BAD_REQUEST, "Medication name '" + medicationDTO.getMedicationName() + "' is already taken.");
                }
            });
        }
        modelMapper.map(medicationDTO, existingMedication);
        existingMedication.setId(medicationId);
        return modelMapper.map(medicationRepository.save(existingMedication), MedicationDTO.class);
    }

    @Override
    @Transactional
    public void deleteMedication(Long medicationId) {
        // Add check if medication is used in any active prescriptions if necessary
        Medication medication = medicationRepository.findById(medicationId)
            .orElseThrow(() -> new ResourceNotFoundException("Medication", "id", medicationId));
        medicationRepository.delete(medication);
    }
}