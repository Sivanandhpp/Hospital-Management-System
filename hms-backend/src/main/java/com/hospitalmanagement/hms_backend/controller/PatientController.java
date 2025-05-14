package com.hospitalmanagement.hms_backend.controller;

import com.hospitalmanagement.hms_backend.dto.PatientDTO;
import com.hospitalmanagement.hms_backend.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    @PostMapping
    public ResponseEntity<PatientDTO> createPatient(@Valid @RequestBody PatientDTO patientDTO) {
        PatientDTO createdPatient = patientService.createPatient(patientDTO);
        return new ResponseEntity<>(createdPatient, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR', 'NURSE')")
    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable(name = "id") Long patientId) {
        return ResponseEntity.ok(patientService.getPatientById(patientId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR', 'NURSE')")
    @GetMapping
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR', 'NURSE')")
    @GetMapping("/search")
    public ResponseEntity<List<PatientDTO>> searchPatients(@RequestParam("query") String query) {
        return ResponseEntity.ok(patientService.searchPatients(query));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable(name = "id") Long patientId,
                                                    @Valid @RequestBody PatientDTO patientDTO) {
        PatientDTO updatedPatient = patientService.updatePatient(patientId, patientDTO);
        return ResponseEntity.ok(updatedPatient);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatient(@PathVariable(name = "id") Long patientId) {
        patientService.deletePatient(patientId);
        return ResponseEntity.ok("Patient deleted successfully.");
    }
}