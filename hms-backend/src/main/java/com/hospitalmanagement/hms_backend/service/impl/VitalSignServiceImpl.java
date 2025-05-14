package com.hospitalmanagement.hms_backend.service.impl;

import com.hospitalmanagement.hms_backend.dto.VitalSignDTO;
import com.hospitalmanagement.hms_backend.entity.Encounter;
import com.hospitalmanagement.hms_backend.entity.VitalSign;
import com.hospitalmanagement.hms_backend.exception.ResourceNotFoundException;
import com.hospitalmanagement.hms_backend.repository.EncounterRepository;
import com.hospitalmanagement.hms_backend.repository.VitalSignRepository;
import com.hospitalmanagement.hms_backend.service.VitalSignService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VitalSignServiceImpl implements VitalSignService {

    private final VitalSignRepository vitalSignRepository;
    private final EncounterRepository encounterRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public VitalSignDTO recordVitalSign(VitalSignDTO dto) {
        Encounter encounter = encounterRepository.findById(dto.getEncounterId())
                .orElseThrow(() -> new ResourceNotFoundException("Encounter", "id", dto.getEncounterId()));
        VitalSign vitalSign = modelMapper.map(dto, VitalSign.class);
        vitalSign.setEncounter(encounter);

        // Calculate BMI if height and weight are present
        if (vitalSign.getHeightCm() != null && vitalSign.getWeightKg() != null &&
            vitalSign.getHeightCm().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal heightInMeters = vitalSign.getHeightCm().divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            BigDecimal bmi = vitalSign.getWeightKg().divide(heightInMeters.multiply(heightInMeters), 1, RoundingMode.HALF_UP);
            vitalSign.setBmi(bmi);
        }

        VitalSign savedVitalSign = vitalSignRepository.save(vitalSign);
        return modelMapper.map(savedVitalSign, VitalSignDTO.class);
    }

    @Override
    public VitalSignDTO getVitalSignById(Long vitalSignId) {
        VitalSign vitalSign = vitalSignRepository.findById(vitalSignId)
                .orElseThrow(() -> new ResourceNotFoundException("VitalSign", "id", vitalSignId));
        return modelMapper.map(vitalSign, VitalSignDTO.class);
    }

    @Override
    public List<VitalSignDTO> getVitalSignsByEncounterId(Long encounterId) {
        encounterRepository.findById(encounterId)
            .orElseThrow(() -> new ResourceNotFoundException("Encounter", "id", encounterId));
        return vitalSignRepository.findByEncounterIdOrderByRecordedAtDesc(encounterId).stream()
                .map(vital -> modelMapper.map(vital, VitalSignDTO.class))
                .collect(Collectors.toList());
    }
}