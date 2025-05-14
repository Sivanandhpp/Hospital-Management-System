package com.hospitalmanagement.hms_backend.service;

import com.hospitalmanagement.hms_backend.dto.LabTestDTO;
import java.util.List;

public interface LabTestService {
    LabTestDTO addLabTest(LabTestDTO labTestDTO);
    LabTestDTO getLabTestById(Long labTestId);
    List<LabTestDTO> getAllLabTests();
    List<LabTestDTO> searchLabTests(String name);
    List<LabTestDTO> getLabTestsByCategory(String category);
    LabTestDTO updateLabTest(Long labTestId, LabTestDTO labTestDTO);
    void deleteLabTest(Long labTestId);
}