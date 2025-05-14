package com.hospitalmanagement.hms_backend.service;

import com.hospitalmanagement.hms_backend.dto.LabOrderDTO;
import com.hospitalmanagement.hms_backend.dto.LabOrderResultUpdateDTO;
import java.util.List;

public interface LabOrderService {
    LabOrderDTO createLabOrder(LabOrderDTO labOrderDTO);
    LabOrderDTO getLabOrderById(Long labOrderId);
    List<LabOrderDTO> getLabOrdersByEncounterId(Long encounterId);
    List<LabOrderDTO> getLabOrdersByPatientId(Long patientId);
    LabOrderDTO updateLabOrderStatus(Long labOrderId, String status);
    LabOrderDTO updateLabOrderItemResult(Long labOrderId, Long labOrderItemId, LabOrderResultUpdateDTO resultDTO);
    // Deletion of lab orders might be restricted.
}