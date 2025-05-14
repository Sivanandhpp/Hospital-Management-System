package com.hospitalmanagement.hms_backend.service;

import com.hospitalmanagement.hms_backend.dto.BedDTO;
import java.util.List;

public interface BedService {
    BedDTO createBed(BedDTO bedDTO);
    BedDTO getBedById(Long bedId);
    List<BedDTO> getAllBeds();
    List<BedDTO> getBedsByWardId(Integer wardId);
    List<BedDTO> getAvailableBedsByWardId(Integer wardId);
    BedDTO updateBed(Long bedId, BedDTO bedDTO);
    void deleteBed(Long bedId); // Consider implications: if bed is part of an active admission
    BedDTO updateBedOccupancy(Long bedId, boolean isOccupied);
}