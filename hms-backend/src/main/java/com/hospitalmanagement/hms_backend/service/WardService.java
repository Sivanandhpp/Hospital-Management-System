package com.hospitalmanagement.hms_backend.service;

import com.hospitalmanagement.hms_backend.dto.WardDTO;
import java.util.List;

public interface WardService {
    WardDTO createWard(WardDTO wardDTO);
    WardDTO getWardById(Integer wardId);
    List<WardDTO> getAllWards();
    WardDTO updateWard(Integer wardId, WardDTO wardDTO);
    void deleteWard(Integer wardId); // Consider implications: if beds exist in this ward
}