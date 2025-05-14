package com.hospitalmanagement.hms_backend.service.impl;

import com.hospitalmanagement.hms_backend.dto.WardDTO;
import com.hospitalmanagement.hms_backend.entity.Ward;
import com.hospitalmanagement.hms_backend.exception.HospitalAPIException;
import com.hospitalmanagement.hms_backend.exception.ResourceNotFoundException;
import com.hospitalmanagement.hms_backend.repository.BedRepository;
import com.hospitalmanagement.hms_backend.repository.WardRepository;
import com.hospitalmanagement.hms_backend.service.WardService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WardServiceImpl implements WardService {

    private final WardRepository wardRepository;
    private final BedRepository bedRepository; // To check if beds exist before deleting a ward
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public WardDTO createWard(WardDTO wardDTO) {
        wardRepository.findByWardName(wardDTO.getWardName()).ifPresent(w -> {
            throw new HospitalAPIException(HttpStatus.BAD_REQUEST, "Ward with name '" + wardDTO.getWardName() + "' already exists.");
        });
        Ward ward = modelMapper.map(wardDTO, Ward.class);
        Ward savedWard = wardRepository.save(ward);
        return modelMapper.map(savedWard, WardDTO.class);
    }

    @Override
    public WardDTO getWardById(Integer wardId) {
        Ward ward = wardRepository.findById(wardId)
                .orElseThrow(() -> new ResourceNotFoundException("Ward", "id", wardId));
        return modelMapper.map(ward, WardDTO.class);
    }

    @Override
    public List<WardDTO> getAllWards() {
        return wardRepository.findAll().stream()
                .map(ward -> modelMapper.map(ward, WardDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public WardDTO updateWard(Integer wardId, WardDTO wardDTO) {
        Ward existingWard = wardRepository.findById(wardId)
                .orElseThrow(() -> new ResourceNotFoundException("Ward", "id", wardId));

        if (wardDTO.getWardName() != null && !wardDTO.getWardName().equals(existingWard.getWardName())) {
            wardRepository.findByWardName(wardDTO.getWardName()).ifPresent(w -> {
                if (!w.getId().equals(wardId)) {
                    throw new HospitalAPIException(HttpStatus.BAD_REQUEST, "Ward with name '" + wardDTO.getWardName() + "' already exists.");
                }
            });
        }
        modelMapper.map(wardDTO, existingWard);
        existingWard.setId(wardId); // Ensure ID isn't changed
        Ward updatedWard = wardRepository.save(existingWard);
        return modelMapper.map(updatedWard, WardDTO.class);
    }

    @Override
    @Transactional
    public void deleteWard(Integer wardId) {
        Ward ward = wardRepository.findById(wardId)
                .orElseThrow(() -> new ResourceNotFoundException("Ward", "id", wardId));
        // Check if any beds are associated with this ward
        if (!bedRepository.findByWardId(wardId).isEmpty()) {
            throw new HospitalAPIException(HttpStatus.BAD_REQUEST, "Cannot delete ward. Beds are still associated with this ward.");
        }
        wardRepository.delete(ward);
    }
}