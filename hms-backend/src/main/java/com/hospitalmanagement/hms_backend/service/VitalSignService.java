package com.hospitalmanagement.hms_backend.service;

import com.hospitalmanagement.hms_backend.dto.VitalSignDTO;
import java.util.List;

public interface VitalSignService {
    VitalSignDTO recordVitalSign(VitalSignDTO vitalSignDTO);
    VitalSignDTO getVitalSignById(Long vitalSignId);
    List<VitalSignDTO> getVitalSignsByEncounterId(Long encounterId);
    // Update/Delete might be restricted for historical data integrity
}