package com.hospitalmanagement.hms_backend.controller;

import com.hospitalmanagement.hms_backend.dto.DiagnosisDTO;
import com.hospitalmanagement.hms_backend.service.DiagnosisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/diagnoses")
@RequiredArgsConstructor
public class DiagnosisController {

    private final DiagnosisService diagnosisService;

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')") // Primarily doctors
    @PostMapping
    public ResponseEntity<DiagnosisDTO> addDiagnosis(@Valid @RequestBody DiagnosisDTO diagnosisDTO) {
        DiagnosisDTO addedDiagnosis = diagnosisService.addDiagnosis(diagnosisDTO);
        return new ResponseEntity<>(addedDiagnosis, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    @GetMapping("/{id}")
    public ResponseEntity<DiagnosisDTO> getDiagnosisById(@PathVariable Long id) {
        return ResponseEntity.ok(diagnosisService.getDiagnosisById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    @GetMapping("/encounter/{encounterId}")
    public ResponseEntity<List<DiagnosisDTO>> getDiagnosesByEncounterId(@PathVariable Long encounterId) {
        return ResponseEntity.ok(diagnosisService.getDiagnosesByEncounterId(encounterId));
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<DiagnosisDTO>> getDiagnosesByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(diagnosisService.getDiagnosesByPatientId(patientId));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<DiagnosisDTO> updateDiagnosis(@PathVariable Long id, @Valid @RequestBody DiagnosisDTO diagnosisDTO) {
        return ResponseEntity.ok(diagnosisService.updateDiagnosis(id, diagnosisDTO));
    }
}