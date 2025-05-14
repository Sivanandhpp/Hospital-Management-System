package com.hospitalmanagement.hms_backend.controller;

import com.hospitalmanagement.hms_backend.dto.LabTestDTO;
import com.hospitalmanagement.hms_backend.service.LabTestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lab-tests") // Catalog management
@RequiredArgsConstructor
public class LabTestController {
    private final LabTestService labTestService;

    @PreAuthorize("hasRole('ADMIN')") // Or LAB_MANAGER if such role exists
    @PostMapping
    public ResponseEntity<LabTestDTO> addLabTest(@Valid @RequestBody LabTestDTO labTestDTO) {
        return new ResponseEntity<>(labTestService.addLabTest(labTestDTO), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST', 'LAB_TECHNICIAN')") // For lookups
    @GetMapping("/{id}")
    public ResponseEntity<LabTestDTO> getLabTestById(@PathVariable Long id) {
        return ResponseEntity.ok(labTestService.getLabTestById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST', 'LAB_TECHNICIAN')")
    @GetMapping
    public ResponseEntity<List<LabTestDTO>> getAllOrSearchLabTests(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category) {
        if (name != null && !name.isEmpty()) {
            return ResponseEntity.ok(labTestService.searchLabTests(name));
        }
        if (category != null && !category.isEmpty()) {
            return ResponseEntity.ok(labTestService.getLabTestsByCategory(category));
        }
        return ResponseEntity.ok(labTestService.getAllLabTests());
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<LabTestDTO> updateLabTest(@PathVariable Long id, @Valid @RequestBody LabTestDTO labTestDTO) {
        return ResponseEntity.ok(labTestService.updateLabTest(id, labTestDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLabTest(@PathVariable Long id) {
        labTestService.deleteLabTest(id);
        return ResponseEntity.ok("Lab Test deleted successfully.");
    }
}