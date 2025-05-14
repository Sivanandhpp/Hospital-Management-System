package com.hospitalmanagement.hms_backend.controller;

import com.hospitalmanagement.hms_backend.dto.PrescriptionDTO;
import com.hospitalmanagement.hms_backend.service.PrescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {
    private final PrescriptionService prescriptionService;

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @PostMapping
    public ResponseEntity<PrescriptionDTO> createPrescription(@Valid @RequestBody PrescriptionDTO prescriptionDTO) {
        return new ResponseEntity<>(prescriptionService.createPrescription(prescriptionDTO), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')") // Patient might view their own via a patient portal
    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionDTO> getPrescriptionById(@PathVariable Long id) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    @GetMapping("/encounter/{encounterId}")
    public ResponseEntity<PrescriptionDTO> getPrescriptionByEncounterId(@PathVariable Long encounterId) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionByEncounterId(encounterId));
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PrescriptionDTO>> getPrescriptionsByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionsByPatientId(patientId));
    }
}