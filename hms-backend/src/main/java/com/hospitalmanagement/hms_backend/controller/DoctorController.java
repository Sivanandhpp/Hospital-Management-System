package com.hospitalmanagement.hms_backend.controller;

import com.hospitalmanagement.hms_backend.dto.DoctorDTO;
import com.hospitalmanagement.hms_backend.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    // Typically only Admin creates doctor profiles after user (doctor) account exists
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<DoctorDTO> createDoctor(@Valid @RequestBody DoctorDTO doctorDTO) {
        DoctorDTO createdDoctor = doctorService.createDoctor(doctorDTO);
        return new ResponseEntity<>(createdDoctor, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR', 'NURSE')")
    @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable(name = "id") Long doctorId) {
        return ResponseEntity.ok(doctorService.getDoctorById(doctorId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR', 'NURSE')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<DoctorDTO> getDoctorByUserId(@PathVariable(name = "userId") Long userId) {
        return ResponseEntity.ok(doctorService.getDoctorByUserId(userId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR', 'NURSE')")
    @GetMapping
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR', 'NURSE')")
    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<List<DoctorDTO>> findDoctorsBySpecialization(@PathVariable String specialization) {
        return ResponseEntity.ok(doctorService.findDoctorsBySpecialization(specialization));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<DoctorDTO> updateDoctor(@PathVariable(name = "id") Long doctorId,
                                                  @Valid @RequestBody DoctorDTO doctorDTO) {
        DoctorDTO updatedDoctor = doctorService.updateDoctor(doctorId, doctorDTO);
        return ResponseEntity.ok(updatedDoctor);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoctor(@PathVariable(name = "id") Long doctorId) {
        doctorService.deleteDoctor(doctorId);
        return ResponseEntity.ok("Doctor profile deleted successfully.");
    }
}