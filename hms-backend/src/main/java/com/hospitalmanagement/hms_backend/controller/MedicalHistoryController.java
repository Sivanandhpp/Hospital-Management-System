package com.hospitalmanagement.hms_backend.controller;

import com.hospitalmanagement.hms_backend.dto.MedicalHistoryDTO;
import com.hospitalmanagement.hms_backend.service.MedicalHistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medical-history")
@RequiredArgsConstructor
public class MedicalHistoryController {

    private final MedicalHistoryService medicalHistoryService;

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    @PostMapping
    public ResponseEntity<MedicalHistoryDTO> addMedicalHistory(@Valid @RequestBody MedicalHistoryDTO medicalHistoryDTO) {
        MedicalHistoryDTO addedHistory = medicalHistoryService.addMedicalHistory(medicalHistoryDTO);
        return new ResponseEntity<>(addedHistory, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    @GetMapping("/{id}")
    public ResponseEntity<MedicalHistoryDTO> getMedicalHistoryById(@PathVariable Long id) {
        return ResponseEntity.ok(medicalHistoryService.getMedicalHistoryById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalHistoryDTO>> getMedicalHistoryByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(medicalHistoryService.getMedicalHistoryByPatientId(patientId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    @GetMapping("/patient/{patientId}/type/{historyType}")
    public ResponseEntity<List<MedicalHistoryDTO>> getMedicalHistoryByPatientIdAndType(
            @PathVariable Long patientId, @PathVariable String historyType) {
        return ResponseEntity.ok(medicalHistoryService.getMedicalHistoryByPatientIdAndType(patientId, historyType));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    @PutMapping("/{id}")
    public ResponseEntity<MedicalHistoryDTO> updateMedicalHistory(@PathVariable Long id, @Valid @RequestBody MedicalHistoryDTO medicalHistoryDTO) {
        return ResponseEntity.ok(medicalHistoryService.updateMedicalHistory(id, medicalHistoryDTO));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')") // More restrictive for delete
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMedicalHistory(@PathVariable Long id) {
        medicalHistoryService.deleteMedicalHistory(id);
        return ResponseEntity.ok("Medical history record deleted successfully.");
    }
}