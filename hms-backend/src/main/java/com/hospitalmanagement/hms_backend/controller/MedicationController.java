package com.hospitalmanagement.hms_backend.controller;

import com.hospitalmanagement.hms_backend.dto.MedicationDTO;
import com.hospitalmanagement.hms_backend.service.MedicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medications")
@RequiredArgsConstructor
public class MedicationController {
    private final MedicationService medicationService;

    @PreAuthorize("hasRole('ADMIN')") // Or PHARMACIST if that role exists
    @PostMapping
    public ResponseEntity<MedicationDTO> addMedication(@Valid @RequestBody MedicationDTO medicationDTO) {
        return new ResponseEntity<>(medicationService.addMedication(medicationDTO), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')") // For lookups
    @GetMapping("/{id}")
    public ResponseEntity<MedicationDTO> getMedicationById(@PathVariable Long id) {
        return ResponseEntity.ok(medicationService.getMedicationById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    @GetMapping
    public ResponseEntity<List<MedicationDTO>> getAllOrSearchMedications(
            @RequestParam(required = false) String name) {
        if (name != null && !name.isEmpty()) {
            return ResponseEntity.ok(medicationService.searchMedications(name));
        }
        return ResponseEntity.ok(medicationService.getAllMedications());
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<MedicationDTO> updateMedication(@PathVariable Long id, @Valid @RequestBody MedicationDTO medicationDTO) {
        return ResponseEntity.ok(medicationService.updateMedication(id, medicationDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMedication(@PathVariable Long id) {
        medicationService.deleteMedication(id);
        return ResponseEntity.ok("Medication deleted successfully.");
    }
}