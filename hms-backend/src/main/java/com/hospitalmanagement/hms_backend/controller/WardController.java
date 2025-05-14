package com.hospitalmanagement.hms_backend.controller;

import com.hospitalmanagement.hms_backend.dto.WardDTO;
import com.hospitalmanagement.hms_backend.service.WardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wards")
@RequiredArgsConstructor
public class WardController {

    private final WardService wardService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<WardDTO> createWard(@Valid @RequestBody WardDTO wardDTO) {
        WardDTO createdWard = wardService.createWard(wardDTO);
        return new ResponseEntity<>(createdWard, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR', 'NURSE')")
    @GetMapping("/{id}")
    public ResponseEntity<WardDTO> getWardById(@PathVariable Integer id) {
        return ResponseEntity.ok(wardService.getWardById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR', 'NURSE')")
    @GetMapping
    public ResponseEntity<List<WardDTO>> getAllWards() {
        return ResponseEntity.ok(wardService.getAllWards());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<WardDTO> updateWard(@PathVariable Integer id, @Valid @RequestBody WardDTO wardDTO) {
        return ResponseEntity.ok(wardService.updateWard(id, wardDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWard(@PathVariable Integer id) {
        wardService.deleteWard(id);
        return ResponseEntity.ok("Ward deleted successfully.");
    }
}