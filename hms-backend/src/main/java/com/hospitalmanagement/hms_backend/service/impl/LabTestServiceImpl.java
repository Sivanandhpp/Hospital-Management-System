package com.hospitalmanagement.hms_backend.service.impl;

import com.hospitalmanagement.hms_backend.dto.LabTestDTO;
import com.hospitalmanagement.hms_backend.entity.LabTest;
import com.hospitalmanagement.hms_backend.exception.HospitalAPIException;
import com.hospitalmanagement.hms_backend.exception.ResourceNotFoundException;
import com.hospitalmanagement.hms_backend.repository.LabTestRepository;
import com.hospitalmanagement.hms_backend.service.LabTestService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LabTestServiceImpl implements LabTestService {
    private final LabTestRepository labTestRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public LabTestDTO addLabTest(LabTestDTO dto) {
        labTestRepository.findByTestName(dto.getTestName()).ifPresent(lt -> {
             throw new HospitalAPIException(HttpStatus.BAD_REQUEST, "Lab Test with name '" + dto.getTestName() + "' already exists.");
        });
        LabTest labTest = modelMapper.map(dto, LabTest.class);
        return modelMapper.map(labTestRepository.save(labTest), LabTestDTO.class);
    }
    // Implement other methods: getById, getAll, search, getByCategory, update, delete
    // ... (similar structure to MedicationServiceImpl) ...

    @Override
    public LabTestDTO getLabTestById(Long labTestId) {
        LabTest labTest = labTestRepository.findById(labTestId)
            .orElseThrow(() -> new ResourceNotFoundException("LabTest", "id", labTestId));
        return modelMapper.map(labTest, LabTestDTO.class);
    }

    @Override
    public List<LabTestDTO> getAllLabTests() {
        return labTestRepository.findAll().stream()
            .map(lt -> modelMapper.map(lt, LabTestDTO.class))
            .collect(Collectors.toList());
    }

    @Override
    public List<LabTestDTO> searchLabTests(String name) {
        return labTestRepository.findByTestNameContainingIgnoreCase(name).stream()
            .map(lt -> modelMapper.map(lt, LabTestDTO.class))
            .collect(Collectors.toList());
    }

    @Override
    public List<LabTestDTO> getLabTestsByCategory(String category) {
        return labTestRepository.findByCategory(category).stream()
            .map(lt -> modelMapper.map(lt, LabTestDTO.class))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LabTestDTO updateLabTest(Long labTestId, LabTestDTO labTestDTO) {
        LabTest existingLabTest = labTestRepository.findById(labTestId)
            .orElseThrow(() -> new ResourceNotFoundException("LabTest", "id", labTestId));
        
        if(labTestDTO.getTestName() != null && !labTestDTO.getTestName().equals(existingLabTest.getTestName())) {
            labTestRepository.findByTestName(labTestDTO.getTestName()).ifPresent(lt -> {
                if (!lt.getId().equals(labTestId)) {
                    throw new HospitalAPIException(HttpStatus.BAD_REQUEST, "Lab Test name '" + labTestDTO.getTestName() + "' is already taken.");
                }
            });
        }
        modelMapper.map(labTestDTO, existingLabTest);
        existingLabTest.setId(labTestId);
        return modelMapper.map(labTestRepository.save(existingLabTest), LabTestDTO.class);
    }

    @Override
    @Transactional
    public void deleteLabTest(Long labTestId) {
        // Check if lab test is used in any lab orders if needed
        LabTest labTest = labTestRepository.findById(labTestId)
            .orElseThrow(() -> new ResourceNotFoundException("LabTest", "id", labTestId));
        labTestRepository.delete(labTest);
    }
}