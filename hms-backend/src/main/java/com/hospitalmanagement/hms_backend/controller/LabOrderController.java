package com.hospitalmanagement.hms_backend.controller;

import com.hospitalmanagement.hms_backend.dto.LabOrderDTO;
import com.hospitalmanagement.hms_backend.dto.LabOrderResultUpdateDTO;
import com.hospitalmanagement.hms_backend.service.LabOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lab-orders")
@RequiredArgsConstructor
public class LabOrderController {
    private final LabOrderService labOrderService;

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')") // Doctor/Nurse can order tests
    @PostMapping
    public ResponseEntity<LabOrderDTO> createLabOrder(@Valid @RequestBody LabOrderDTO labOrderDTO) {
        return new ResponseEntity<>(labOrderService.createLabOrder(labOrderDTO), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'LAB_TECHNICIAN')")
    @GetMapping("/{id}")
    public ResponseEntity<LabOrderDTO> getLabOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(labOrderService.getLabOrderById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'LAB_TECHNICIAN')")
    @GetMapping("/encounter/{encounterId}")
    public ResponseEntity<List<LabOrderDTO>> getLabOrdersByEncounterId(@PathVariable Long encounterId) {
        return ResponseEntity.ok(labOrderService.getLabOrdersByEncounterId(encounterId));
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'LAB_TECHNICIAN')")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<LabOrderDTO>> getLabOrdersByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(labOrderService.getLabOrdersByPatientId(patientId));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'LAB_TECHNICIAN', 'NURSE')") // Nurse might update status
    @PatchMapping("/{id}/status")
    public ResponseEntity<LabOrderDTO> updateLabOrderStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(labOrderService.updateLabOrderStatus(id, status));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'LAB_TECHNICIAN')") // Lab tech updates results
    @PatchMapping("/{labOrderId}/item/{labOrderItemId}/result")
    public ResponseEntity<LabOrderDTO> updateLabOrderItemResult(
            @PathVariable Long labOrderId,
            @PathVariable Long labOrderItemId,
            @Valid @RequestBody LabOrderResultUpdateDTO resultDTO) {
        return ResponseEntity.ok(labOrderService.updateLabOrderItemResult(labOrderId, labOrderItemId, resultDTO));
    }
}