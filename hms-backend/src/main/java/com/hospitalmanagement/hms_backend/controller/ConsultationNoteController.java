package com.hospitalmanagement.hms_backend.controller;

import com.hospitalmanagement.hms_backend.dto.ConsultationNoteDTO;
import com.hospitalmanagement.hms_backend.service.ConsultationNoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/consultation-notes")
@RequiredArgsConstructor
public class ConsultationNoteController {

    private final ConsultationNoteService consultationNoteService;

    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')") // Primarily doctors
    @PostMapping
    public ResponseEntity<ConsultationNoteDTO> saveOrUpdateConsultationNote(@Valid @RequestBody ConsultationNoteDTO consultationNoteDTO) {
        ConsultationNoteDTO savedNote = consultationNoteService.saveOrUpdateConsultationNote(consultationNoteDTO);
        return ResponseEntity.ok(savedNote);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    @GetMapping("/encounter/{encounterId}")
    public ResponseEntity<ConsultationNoteDTO> getConsultationNoteByEncounterId(@PathVariable Long encounterId) {
        return ResponseEntity.ok(consultationNoteService.getConsultationNoteByEncounterId(encounterId));
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    @GetMapping("/{id}")
    public ResponseEntity<ConsultationNoteDTO> getConsultationNoteById(@PathVariable Long id) {
        return ResponseEntity.ok(consultationNoteService.getConsultationNoteById(id));
    }


    @PreAuthorize("hasRole('ADMIN')") // Deletion might be admin only
    @DeleteMapping("/encounter/{encounterId}")
    public ResponseEntity<String> deleteConsultationNoteByEncounterId(@PathVariable Long encounterId) {
        consultationNoteService.deleteConsultationNoteByEncounterId(encounterId);
        return ResponseEntity.ok("Consultation note for encounter deleted successfully.");
    }
}