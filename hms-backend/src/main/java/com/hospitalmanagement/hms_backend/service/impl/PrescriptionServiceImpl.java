package com.hospitalmanagement.hms_backend.service.impl;

import com.hospitalmanagement.hms_backend.dto.PrescriptionDTO;
import com.hospitalmanagement.hms_backend.dto.PrescriptionItemDTO;
import com.hospitalmanagement.hms_backend.entity.Encounter;
import com.hospitalmanagement.hms_backend.entity.Medication;
import com.hospitalmanagement.hms_backend.entity.Prescription;
import com.hospitalmanagement.hms_backend.entity.PrescriptionItem;
import com.hospitalmanagement.hms_backend.exception.HospitalAPIException;
import com.hospitalmanagement.hms_backend.exception.ResourceNotFoundException;
import com.hospitalmanagement.hms_backend.repository.EncounterRepository;
import com.hospitalmanagement.hms_backend.repository.MedicationRepository;
import com.hospitalmanagement.hms_backend.repository.PrescriptionRepository;
import com.hospitalmanagement.hms_backend.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {
    private final PrescriptionRepository prescriptionRepository;
    private final EncounterRepository encounterRepository;
    private final MedicationRepository medicationRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public PrescriptionDTO createPrescription(PrescriptionDTO dto) {
        Encounter encounter = encounterRepository.findById(dto.getEncounterId())
                .orElseThrow(() -> new ResourceNotFoundException("Encounter", "id", dto.getEncounterId()));

        // Check if a prescription already exists for this encounter (usually one per encounter)
        prescriptionRepository.findByEncounterId(dto.getEncounterId()).ifPresent(p -> {
            throw new HospitalAPIException(HttpStatus.BAD_REQUEST, "Prescription already exists for this encounter.");
        });

        Prescription prescription = new Prescription();
        prescription.setEncounter(encounter);
        prescription.setPrescriptionDate(dto.getPrescriptionDate());
        prescription.setNotes(dto.getNotes());

        List<PrescriptionItem> items = dto.getItems().stream().map(itemDto -> {
            Medication medication = medicationRepository.findById(itemDto.getMedicationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Medication", "id", itemDto.getMedicationId()));
            PrescriptionItem item = new PrescriptionItem();
            item.setMedication(medication);
            item.setDosage(itemDto.getDosage());
            item.setFrequency(itemDto.getFrequency());
            item.setDuration(itemDto.getDuration());
            item.setRoute(itemDto.getRoute());
            item.setInstructions(itemDto.getInstructions());
            item.setPrescription(prescription); // Link back
            return item;
        }).collect(Collectors.toList());
        prescription.setItems(items);

        Prescription savedPrescription = prescriptionRepository.save(prescription);
        return mapToDTO(savedPrescription);
    }

    @Override
    public PrescriptionDTO getPrescriptionById(Long prescriptionId) {
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription", "id", prescriptionId));
        return mapToDTO(prescription);
    }

    @Override
    public PrescriptionDTO getPrescriptionByEncounterId(Long encounterId) {
        encounterRepository.findById(encounterId) // Ensure encounter exists
                .orElseThrow(() -> new ResourceNotFoundException("Encounter", "id", encounterId));
        Prescription prescription = prescriptionRepository.findByEncounterId(encounterId)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found for encounter ID: " + encounterId));
        return mapToDTO(prescription);
    }

    @Override
    public List<PrescriptionDTO> getPrescriptionsByPatientId(Long patientId) {
        // Patient patient = patientRepository.findById(patientId)
        //        .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", patientId));
        return prescriptionRepository.findAllByEncounterPatientId(patientId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private PrescriptionDTO mapToDTO(Prescription prescription) {
        PrescriptionDTO dto = modelMapper.map(prescription, PrescriptionDTO.class);
        dto.setEncounterId(prescription.getEncounter().getId());
        dto.setItems(prescription.getItems().stream().map(item -> {
            PrescriptionItemDTO itemDto = modelMapper.map(item, PrescriptionItemDTO.class);
            itemDto.setMedicationId(item.getMedication().getId());
            itemDto.setMedicationName(item.getMedication().getMedicationName());
            return itemDto;
        }).collect(Collectors.toList()));
        return dto;
    }
}