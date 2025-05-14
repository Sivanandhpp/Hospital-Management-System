package com.hospitalmanagement.hms_backend.service.impl;

import com.hospitalmanagement.hms_backend.dto.AdmissionDTO;
import com.hospitalmanagement.hms_backend.dto.DischargeRequestDTO;
import com.hospitalmanagement.hms_backend.entity.*;
import com.hospitalmanagement.hms_backend.enums.AdmissionStatus;
import com.hospitalmanagement.hms_backend.exception.HospitalAPIException;
import com.hospitalmanagement.hms_backend.exception.ResourceNotFoundException;
import com.hospitalmanagement.hms_backend.repository.*;
import com.hospitalmanagement.hms_backend.service.AdmissionService;
import com.hospitalmanagement.hms_backend.service.BedService; // To update bed occupancy
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdmissionServiceImpl implements AdmissionService {

    private final AdmissionRepository admissionRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final WardRepository wardRepository;
    private final BedRepository bedRepository;
    private final EncounterRepository encounterRepository; // Optional: if encounterId is provided
    private final BedService bedService; // For updating bed occupancy
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public AdmissionDTO admitPatient(AdmissionDTO admissionDTO) {
        Patient patient = patientRepository.findById(admissionDTO.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", admissionDTO.getPatientId()));
        Doctor admittingDoctor = doctorRepository.findById(admissionDTO.getAdmittingDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", admissionDTO.getAdmittingDoctorId()));
        Ward ward = wardRepository.findById(admissionDTO.getWardId())
                .orElseThrow(() -> new ResourceNotFoundException("Ward", "id", admissionDTO.getWardId()));
        Bed bed = bedRepository.findById(admissionDTO.getBedId())
                .orElseThrow(() -> new ResourceNotFoundException("Bed", "id", admissionDTO.getBedId()));

        // Check if patient already has an active admission
        admissionRepository.findActiveAdmissionByPatientId(patient.getId()).ifPresent(adm -> {
            throw new HospitalAPIException(HttpStatus.BAD_REQUEST, "Patient already has an active admission (ID: " + adm.getId() + ").");
        });

        // Check if bed is available
        if (bed.isOccupied()) {
            throw new HospitalAPIException(HttpStatus.BAD_REQUEST, "Bed ID " + bed.getId() + " is already occupied.");
        }
        if (!bed.getWard().getId().equals(ward.getId())) {
             throw new HospitalAPIException(HttpStatus.BAD_REQUEST, "Bed ID " + bed.getId() + " does not belong to Ward ID " + ward.getId());
        }


        Admission admission = new Admission();
        admission.setPatient(patient);
        admission.setAdmittingDoctor(admittingDoctor);
        admission.setWard(ward);
        admission.setBed(bed);
        admission.setAdmissionDatetime(admissionDTO.getAdmissionDatetime() != null ? admissionDTO.getAdmissionDatetime() : LocalDateTime.now());
        admission.setReasonForAdmission(admissionDTO.getReasonForAdmission());
        admission.setStatus(AdmissionStatus.ADMITTED);

        if (admissionDTO.getEncounterId() != null) {
            Encounter encounter = encounterRepository.findById(admissionDTO.getEncounterId()).orElse(null);
            // Optionally throw error if encounter not found, or just proceed without it
            admission.setEncounter(encounter);
        }

        Admission savedAdmission = admissionRepository.save(admission);
        bedService.updateBedOccupancy(bed.getId(), true); // Mark bed as occupied

        return mapToDTO(savedAdmission);
    }

    @Override
    public AdmissionDTO getAdmissionById(Long admissionId) {
        Admission admission = admissionRepository.findById(admissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Admission", "id", admissionId));
        return mapToDTO(admission);
    }

    @Override
    public AdmissionDTO getActiveAdmissionByPatientId(Long patientId) {
        patientRepository.findById(patientId)
            .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", patientId));
        Admission admission = admissionRepository.findActiveAdmissionByPatientId(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("No active admission found for patient ID: " + patientId));
        return mapToDTO(admission);
    }

    @Override
    public List<AdmissionDTO> getAllAdmissions() {
        return admissionRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AdmissionDTO> getAdmissionsByPatientId(Long patientId) {
        patientRepository.findById(patientId)
            .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", patientId));
        return admissionRepository.findByPatientId(patientId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AdmissionDTO> getAdmissionsByWardId(Integer wardId) {
        wardRepository.findById(wardId)
            .orElseThrow(() -> new ResourceNotFoundException("Ward", "id", wardId));
        return admissionRepository.findByWardId(wardId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AdmissionDTO dischargePatient(Long admissionId, DischargeRequestDTO dischargeRequestDTO) {
        Admission admission = admissionRepository.findById(admissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Admission", "id", admissionId));

        if (admission.getStatus() == AdmissionStatus.DISCHARGED) {
            throw new HospitalAPIException(HttpStatus.BAD_REQUEST, "Patient is already discharged for this admission.");
        }
        if (dischargeRequestDTO.getDischargeDatetime().isBefore(admission.getAdmissionDatetime())) {
            throw new HospitalAPIException(HttpStatus.BAD_REQUEST, "Discharge date cannot be before admission date.");
        }


        admission.setDischargeDatetime(dischargeRequestDTO.getDischargeDatetime());
        admission.setDischargeSummary(dischargeRequestDTO.getDischargeSummary());
        admission.setStatus(AdmissionStatus.DISCHARGED);

        Admission updatedAdmission = admissionRepository.save(admission);
        bedService.updateBedOccupancy(admission.getBed().getId(), false); // Mark bed as not occupied

        return mapToDTO(updatedAdmission);
    }

    private AdmissionDTO mapToDTO(Admission admission) {
        AdmissionDTO dto = modelMapper.map(admission, AdmissionDTO.class);
        if (admission.getPatient() != null) {
            dto.setPatientId(admission.getPatient().getId());
            dto.setPatientFullName(admission.getPatient().getFirstName() + " " + admission.getPatient().getLastName());
        }
        if (admission.getAdmittingDoctor() != null && admission.getAdmittingDoctor().getUser() != null) {
            dto.setAdmittingDoctorId(admission.getAdmittingDoctor().getId());
            dto.setAdmittingDoctorFullName(admission.getAdmittingDoctor().getUser().getFullName());
        }
        if (admission.getWard() != null) {
            dto.setWardId(admission.getWard().getId());
            dto.setWardName(admission.getWard().getWardName());
        }
        if (admission.getBed() != null) {
            dto.setBedId(admission.getBed().getId());
            dto.setBedNumber(admission.getBed().getBedNumber());
        }
        if (admission.getEncounter() != null) {
            dto.setEncounterId(admission.getEncounter().getId());
        }
        return dto;
    }
}