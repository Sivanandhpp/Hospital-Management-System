package com.hospitalmanagement.hms_backend.controller;

import com.hospitalmanagement.hms_backend.dto.EncounterDTO;
import com.hospitalmanagement.hms_backend.service.EncounterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/encounters")
@RequiredArgsConstructor
public class EncounterController {

    private final EncounterService encounterService;

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    @PostMapping
    public ResponseEntity<EncounterDTO> createEncounter(@Valid @RequestBody EncounterDTO encounterDTO) {
        EncounterDTO createdEncounter = encounterService.createEncounter(encounterDTO);
        return new ResponseEntity<>(createdEncounter, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    @GetMapping("/{id}")
    public ResponseEntity<EncounterDTO> getEncounterById(@PathVariable Long id) {
        return ResponseEntity.ok(encounterService.getEncounterById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<EncounterDTO>> getEncountersByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(encounterService.getEncountersByPatientId(patientId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<EncounterDTO>> getEncountersByDoctorId(@PathVariable Long doctorId) {
        return ResponseEntity.ok(encounterService.getEncountersByDoctorId(doctorId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    @PutMapping("/{id}")
    public ResponseEntity<EncounterDTO> updateEncounter(@PathVariable Long id, @Valid @RequestBody EncounterDTO encounterDTO) {
        return ResponseEntity.ok(encounterService.updateEncounter(id, encounterDTO));
    }
}