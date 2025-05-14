package com.hospitalmanagement.hms_backend.controller; // Adjust your package name

import com.hospitalmanagement.hms_backend.dto.VitalSignDTO;
import com.hospitalmanagement.hms_backend.service.VitalSignService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vital-signs")
@RequiredArgsConstructor // For constructor injection of VitalSignService
public class VitalSignController {

    private final VitalSignService vitalSignService;

    /**
     * Records a new set of vital signs for an encounter.
     * @param vitalSignDTO The DTO containing vital sign data.
     * @return The created VitalSignDTO with its generated ID.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    @PostMapping
    public ResponseEntity<VitalSignDTO> recordVitalSign(@Valid @RequestBody VitalSignDTO vitalSignDTO) {
        VitalSignDTO recordedVitalSign = vitalSignService.recordVitalSign(vitalSignDTO);
        return new ResponseEntity<>(recordedVitalSign, HttpStatus.CREATED);
    }

    /**
     * Retrieves a specific vital sign record by its ID.
     * @param id The ID of the vital sign record.
     * @return The VitalSignDTO if found.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    @GetMapping("/{id}")
    public ResponseEntity<VitalSignDTO> getVitalSignById(@PathVariable(name = "id") Long id) {
        VitalSignDTO vitalSignDTO = vitalSignService.getVitalSignById(id);
        return ResponseEntity.ok(vitalSignDTO);
    }

    /**
     * Retrieves all vital signs recorded for a specific encounter, ordered by recorded time descending.
     * @param encounterId The ID of the encounter.
     * @return A list of VitalSignDTOs.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    @GetMapping("/encounter/{encounterId}")
    public ResponseEntity<List<VitalSignDTO>> getVitalSignsByEncounterId(@PathVariable Long encounterId) {
        List<VitalSignDTO> vitalSigns = vitalSignService.getVitalSignsByEncounterId(encounterId);
        return ResponseEntity.ok(vitalSigns);
    }

    // --- Optional: Update and Delete Endpoints ---
    // As discussed, updating/deleting vitals is often restricted.
    // If you need these, implement them in your VitalSignService and uncomment here.

    /**
     * Updates an existing vital sign record. (Use with caution)
     * @param id The ID of the vital sign record to update.
     * @param vitalSignDTO The DTO containing updated data.
     * @return The updated VitalSignDTO.
     */
    /*
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')") // Adjust roles as needed
    @PutMapping("/{id}")
    public ResponseEntity<VitalSignDTO> updateVitalSign(@PathVariable Long id, @Valid @RequestBody VitalSignDTO vitalSignDTO) {
        // Ensure vitalSignDTO might need to carry the id or service handles it
        VitalSignDTO updatedVitalSign = vitalSignService.updateVitalSign(id, vitalSignDTO);
        return ResponseEntity.ok(updatedVitalSign);
    }
    */

    /**
     * Deletes a vital sign record. (Use with extreme caution)
     * @param id The ID of the vital sign record to delete.
     * @return A success message.
     */
    /*
    @PreAuthorize("hasRole('ADMIN')") // Deletion is typically highly restricted
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVitalSign(@PathVariable Long id) {
        vitalSignService.deleteVitalSign(id);
        return ResponseEntity.ok("Vital sign record deleted successfully.");
    }
    */
}