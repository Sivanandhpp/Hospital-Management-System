package com.hospitalmanagement.hms_backend.service.impl;

import com.hospitalmanagement.hms_backend.dto.LabOrderDTO;
import com.hospitalmanagement.hms_backend.dto.LabOrderItemDTO;
import com.hospitalmanagement.hms_backend.dto.LabOrderResultUpdateDTO;
import com.hospitalmanagement.hms_backend.entity.Encounter;
import com.hospitalmanagement.hms_backend.entity.LabOrder;
import com.hospitalmanagement.hms_backend.entity.LabOrderItem;
import com.hospitalmanagement.hms_backend.entity.LabTest;
import com.hospitalmanagement.hms_backend.exception.HospitalAPIException;
import com.hospitalmanagement.hms_backend.exception.ResourceNotFoundException;
import com.hospitalmanagement.hms_backend.repository.EncounterRepository;
import com.hospitalmanagement.hms_backend.repository.LabOrderItemRepository;
import com.hospitalmanagement.hms_backend.repository.LabOrderRepository;
import com.hospitalmanagement.hms_backend.repository.LabTestRepository;
import com.hospitalmanagement.hms_backend.service.LabOrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LabOrderServiceImpl implements LabOrderService {
    private final LabOrderRepository labOrderRepository;
    private final LabOrderItemRepository labOrderItemRepository; // For updating individual items
    private final EncounterRepository encounterRepository;
    private final LabTestRepository labTestRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public LabOrderDTO createLabOrder(LabOrderDTO dto) {
        Encounter encounter = encounterRepository.findById(dto.getEncounterId())
                .orElseThrow(() -> new ResourceNotFoundException("Encounter", "id", dto.getEncounterId()));

        LabOrder labOrder = new LabOrder();
        labOrder.setEncounter(encounter);
        labOrder.setOrderDatetime(dto.getOrderDatetime() != null ? dto.getOrderDatetime() : java.time.LocalDateTime.now());
        labOrder.setStatus(dto.getStatus() != null ? dto.getStatus() : "ORDERED");
        labOrder.setNotes(dto.getNotes());

        List<LabOrderItem> items = dto.getItems().stream().map(itemDto -> {
            LabTest labTest = labTestRepository.findById(itemDto.getLabTestId())
                    .orElseThrow(() -> new ResourceNotFoundException("LabTest", "id", itemDto.getLabTestId()));
            LabOrderItem item = new LabOrderItem();
            item.setLabTest(labTest);
            item.setLabOrder(labOrder); // Link back
            // Result fields are not set at order creation
            return item;
        }).collect(Collectors.toList());
        labOrder.setItems(items);

        LabOrder savedLabOrder = labOrderRepository.save(labOrder);
        return mapToDTO(savedLabOrder);
    }

    @Override
    public LabOrderDTO getLabOrderById(Long labOrderId) {
        LabOrder labOrder = labOrderRepository.findById(labOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("LabOrder", "id", labOrderId));
        return mapToDTO(labOrder);
    }

    @Override
    public List<LabOrderDTO> getLabOrdersByEncounterId(Long encounterId) {
        encounterRepository.findById(encounterId)
            .orElseThrow(() -> new ResourceNotFoundException("Encounter", "id", encounterId));
        return labOrderRepository.findByEncounterId(encounterId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<LabOrderDTO> getLabOrdersByPatientId(Long patientId) {
        return labOrderRepository.findByEncounterPatientId(patientId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public LabOrderDTO updateLabOrderStatus(Long labOrderId, String status) {
        LabOrder labOrder = labOrderRepository.findById(labOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("LabOrder", "id", labOrderId));
        labOrder.setStatus(status);
        return mapToDTO(labOrderRepository.save(labOrder));
    }

    @Override
    @Transactional
    public LabOrderDTO updateLabOrderItemResult(Long labOrderId, Long labOrderItemId, LabOrderResultUpdateDTO resultDTO) {
        LabOrder labOrder = labOrderRepository.findById(labOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("LabOrder", "id", labOrderId));
        LabOrderItem item = labOrderItemRepository.findById(labOrderItemId)
                .orElseThrow(() -> new ResourceNotFoundException("LabOrderItem", "id", labOrderItemId));

        if (!item.getLabOrder().getId().equals(labOrderId)) {
            throw new HospitalAPIException(HttpStatus.BAD_REQUEST, "Lab order item does not belong to the specified lab order.");
        }

        item.setResultValue(resultDTO.getResultValue());
        item.setResultUnit(resultDTO.getResultUnit());
        item.setResultDatetime(resultDTO.getResultDatetime());
        // CORRECTED USAGE OF SETTER
        item.setAbnormal(resultDTO.getAbnormal()); // Now uses setAbnormal
        item.setTechnicianNotes(resultDTO.getTechnicianNotes());
        item.setResultAttachmentPath(resultDTO.getResultAttachmentPath());
        labOrderItemRepository.save(item);

        return mapToDTO(labOrder);
    }

    private LabOrderDTO mapToDTO(LabOrder labOrder) {
        LabOrderDTO dto = modelMapper.map(labOrder, LabOrderDTO.class);
        dto.setEncounterId(labOrder.getEncounter().getId());
        dto.setItems(labOrder.getItems().stream().map(item -> {
            LabOrderItemDTO itemDto = modelMapper.map(item, LabOrderItemDTO.class);
            itemDto.setLabTestId(item.getLabTest().getId());
            itemDto.setLabTestName(item.getLabTest().getTestName());
            return itemDto;
        }).collect(Collectors.toList()));
        return dto;
    }
}