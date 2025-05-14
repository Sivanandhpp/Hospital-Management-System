package com.hospitalmanagement.hms_backend.service.impl;

import com.hospitalmanagement.hms_backend.dto.BedDTO;
import com.hospitalmanagement.hms_backend.entity.Bed;
import com.hospitalmanagement.hms_backend.entity.Ward;
import com.hospitalmanagement.hms_backend.exception.HospitalAPIException;
import com.hospitalmanagement.hms_backend.exception.ResourceNotFoundException;
import com.hospitalmanagement.hms_backend.repository.AdmissionRepository;
import com.hospitalmanagement.hms_backend.repository.BedRepository;
import com.hospitalmanagement.hms_backend.repository.WardRepository;
import com.hospitalmanagement.hms_backend.service.BedService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BedServiceImpl implements BedService {

    private final BedRepository bedRepository;
    private final WardRepository wardRepository;
    private final AdmissionRepository admissionRepository; // To check for active admissions
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public BedDTO createBed(BedDTO bedDTO) {
        Ward ward = wardRepository.findById(bedDTO.getWardId())
                .orElseThrow(() -> new ResourceNotFoundException("Ward", "id", bedDTO.getWardId()));

        bedRepository.findByWardIdAndBedNumber(bedDTO.getWardId(), bedDTO.getBedNumber()).ifPresent(b -> {
            throw new HospitalAPIException(HttpStatus.BAD_REQUEST,
                    "Bed number '" + bedDTO.getBedNumber() + "' already exists in ward '" + ward.getWardName() + "'.");
        });

        Bed bed = new Bed();
        bed.setWard(ward);
        bed.setBedNumber(bedDTO.getBedNumber());
        bed.setBedType(bedDTO.getBedType());
        bed.setNotes(bedDTO.getNotes());
        bed.setOccupied(false); // New beds are initially not occupied

        Bed savedBed = bedRepository.save(bed);
        return mapToDTO(savedBed);
    }

    @Override
    public BedDTO getBedById(Long bedId) {
        Bed bed = bedRepository.findById(bedId)
                .orElseThrow(() -> new ResourceNotFoundException("Bed", "id", bedId));
        return mapToDTO(bed);
    }

    @Override
    public List<BedDTO> getAllBeds() {
        return bedRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BedDTO> getBedsByWardId(Integer wardId) {
        wardRepository.findById(wardId)
            .orElseThrow(() -> new ResourceNotFoundException("Ward", "id", wardId));
        return bedRepository.findByWardId(wardId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BedDTO> getAvailableBedsByWardId(Integer wardId) {
        wardRepository.findById(wardId)
            .orElseThrow(() -> new ResourceNotFoundException("Ward", "id", wardId));
        return bedRepository.findAvailableBedsByWard(wardId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BedDTO updateBed(Long bedId, BedDTO bedDTO) {
        Bed existingBed = bedRepository.findById(bedId)
                .orElseThrow(() -> new ResourceNotFoundException("Bed", "id", bedId));

        // Ward change for a bed is usually not done. If needed, it requires careful handling.
        // For simplicity, we assume ward doesn't change here.

        if (bedDTO.getBedNumber() != null && !bedDTO.getBedNumber().equals(existingBed.getBedNumber())) {
            bedRepository.findByWardIdAndBedNumber(existingBed.getWard().getId(), bedDTO.getBedNumber()).ifPresent(b -> {
                if (!b.getId().equals(bedId)) {
                    throw new HospitalAPIException(HttpStatus.BAD_REQUEST,
                            "Bed number '" + bedDTO.getBedNumber() + "' already exists in ward '" + existingBed.getWard().getWardName() + "'.");
                }
            });
            existingBed.setBedNumber(bedDTO.getBedNumber());
        }

        if (bedDTO.getBedType() != null) {
            existingBed.setBedType(bedDTO.getBedType());
        }
        if (bedDTO.getNotes() != null) {
            existingBed.setNotes(bedDTO.getNotes());
        }
        // isOccupied is typically managed by Admission/Discharge logic

        Bed updatedBed = bedRepository.save(existingBed);
        return mapToDTO(updatedBed);
    }

    @Override
    @Transactional
    public void deleteBed(Long bedId) {
        Bed bed = bedRepository.findById(bedId)
                .orElseThrow(() -> new ResourceNotFoundException("Bed", "id", bedId));

        // Check if bed is part of an active admission
        admissionRepository.findByBedIdAndStatus(bedId, com.hospitalmanagement.hms_backend.enums.AdmissionStatus.ADMITTED)
            .ifPresent(admission -> {
                throw new HospitalAPIException(HttpStatus.BAD_REQUEST, "Cannot delete bed. It is currently occupied in an active admission.");
            });

        bedRepository.delete(bed);
    }
    
    @Override
    @Transactional
    public BedDTO updateBedOccupancy(Long bedId, boolean isOccupied) {
        Bed bed = bedRepository.findById(bedId)
                .orElseThrow(() -> new ResourceNotFoundException("Bed", "id", bedId));
        bed.setOccupied(isOccupied);
        Bed updatedBed = bedRepository.save(bed);
        return mapToDTO(updatedBed);
    }


    private BedDTO mapToDTO(Bed bed) {
        BedDTO dto = modelMapper.map(bed, BedDTO.class);
        if (bed.getWard() != null) {
            dto.setWardId(bed.getWard().getId());
            dto.setWardName(bed.getWard().getWardName());
        }
        return dto;
    }
}