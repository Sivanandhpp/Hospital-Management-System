package com.hospitalmanagement.hms_backend.controller;

import com.hospitalmanagement.hms_backend.dto.BedDTO;
import com.hospitalmanagement.hms_backend.service.BedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/beds")
@RequiredArgsConstructor
public class BedController {

    private final BedService bedService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<BedDTO> createBed(@Valid @RequestBody BedDTO bedDTO) {
        BedDTO createdBed = bedService.createBed(bedDTO);
        return new ResponseEntity<>(createdBed, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR', 'NURSE')")
    @GetMapping("/{id}")
    public ResponseEntity<BedDTO> getBedById(@PathVariable Long id) {
        return ResponseEntity.ok(bedService.getBedById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR', 'NURSE')")
    @GetMapping
    public ResponseEntity<List<BedDTO>> getAllBeds() {
        return ResponseEntity.ok(bedService.getAllBeds());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR', 'NURSE')")
    @GetMapping("/ward/{wardId}")
    public ResponseEntity<List<BedDTO>> getBedsByWardId(@PathVariable Integer wardId) {
        return ResponseEntity.ok(bedService.getBedsByWardId(wardId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR', 'NURSE')")
    @GetMapping("/ward/{wardId}/available")
    public ResponseEntity<List<BedDTO>> getAvailableBedsByWardId(@PathVariable Integer wardId) {
        return ResponseEntity.ok(bedService.getAvailableBedsByWardId(wardId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<BedDTO> updateBed(@PathVariable Long id, @Valid @RequestBody BedDTO bedDTO) {
        return ResponseEntity.ok(bedService.updateBed(id, bedDTO));
    }
    
    @PreAuthorize("hasRole('ADMIN')") // Internal system might call this, or specific roles
    @PatchMapping("/{id}/occupancy")
    public ResponseEntity<BedDTO> updateBedOccupancy(@PathVariable Long id, @RequestParam boolean occupied) {
        return ResponseEntity.ok(bedService.updateBedOccupancy(id, occupied));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBed(@PathVariable Long id) {
        bedService.deleteBed(id);
        return ResponseEntity.ok("Bed deleted successfully.");
    }
}