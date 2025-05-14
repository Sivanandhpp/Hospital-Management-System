package com.hospitalmanagement.hms_backend.controller;

import com.hospitalmanagement.hms_backend.dto.AdmissionDTO;
import com.hospitalmanagement.hms_backend.dto.DischargeRequestDTO;
import com.hospitalmanagement.hms_backend.service.AdmissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admissions")
@RequiredArgsConstructor
public class AdmissionController {

    private final AdmissionService admissionService;

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR')") // Doctor might initiate admission
    @PostMapping("/admit")
    public ResponseEntity<AdmissionDTO> admitPatient(@Valid @RequestBody AdmissionDTO admissionDTO) {
        AdmissionDTO admittedPatient = admissionService.admitPatient(admissionDTO);
        return new ResponseEntity<>(admittedPatient, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR', 'NURSE')")
    @GetMapping("/{id}")
    public ResponseEntity<AdmissionDTO> getAdmissionById(@PathVariable Long id) {
        return ResponseEntity.ok(admissionService.getAdmissionById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR', 'NURSE')")
    @GetMapping("/patient/{patientId}/active")
    public ResponseEntity<AdmissionDTO> getActiveAdmissionByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(admissionService.getActiveAdmissionByPatientId(patientId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')") // Broader view for admin/reception
    @GetMapping
    public ResponseEntity<List<AdmissionDTO>> getAllAdmissions() {
        return ResponseEntity.ok(admissionService.getAllAdmissions());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR', 'NURSE')")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AdmissionDTO>> getAdmissionsByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(admissionService.getAdmissionsByPatientId(patientId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR', 'NURSE')")
    @GetMapping("/ward/{wardId}")
    public ResponseEntity<List<AdmissionDTO>> getAdmissionsByWardId(@PathVariable Integer wardId) {
        return ResponseEntity.ok(admissionService.getAdmissionsByWardId(wardId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')") // Those involved in patient care
    @PostMapping("/{id}/discharge")
    public ResponseEntity<AdmissionDTO> dischargePatient(@PathVariable Long id,
                                                       @Valid @RequestBody DischargeRequestDTO dischargeRequestDTO) {
        AdmissionDTO dischargedPatient = admissionService.dischargePatient(id, dischargeRequestDTO);
        return ResponseEntity.ok(dischargedPatient);
    }
}