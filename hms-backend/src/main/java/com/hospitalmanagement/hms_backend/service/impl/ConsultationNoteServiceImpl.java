package com.hospitalmanagement.hms_backend.service.impl;

import com.hospitalmanagement.hms_backend.dto.ConsultationNoteDTO;
import com.hospitalmanagement.hms_backend.entity.ConsultationNote;
import com.hospitalmanagement.hms_backend.entity.Encounter;
import com.hospitalmanagement.hms_backend.exception.ResourceNotFoundException;
import com.hospitalmanagement.hms_backend.repository.ConsultationNoteRepository;
import com.hospitalmanagement.hms_backend.repository.EncounterRepository;
import com.hospitalmanagement.hms_backend.service.ConsultationNoteService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConsultationNoteServiceImpl implements ConsultationNoteService {

    private final ConsultationNoteRepository consultationNoteRepository;
    private final EncounterRepository encounterRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public ConsultationNoteDTO saveOrUpdateConsultationNote(ConsultationNoteDTO dto) {
        Encounter encounter = encounterRepository.findById(dto.getEncounterId())
                .orElseThrow(() -> new ResourceNotFoundException("Encounter", "id", dto.getEncounterId()));

        ConsultationNote note = consultationNoteRepository.findByEncounterId(dto.getEncounterId())
                .orElse(new ConsultationNote()); // Create new if not exists

        modelMapper.map(dto, note);
        note.setEncounter(encounter); // Ensure encounter is set
        if(note.getId() == null && dto.getId() != null) note.setId(dto.getId()); // If DTO has ID but entity doesn't

        ConsultationNote savedNote = consultationNoteRepository.save(note);
        return modelMapper.map(savedNote, ConsultationNoteDTO.class);
    }

    @Override
    public ConsultationNoteDTO getConsultationNoteByEncounterId(Long encounterId) {
        ConsultationNote note = consultationNoteRepository.findByEncounterId(encounterId)
                .orElseThrow(() -> new ResourceNotFoundException("ConsultationNote", "encounterId", encounterId));
        return modelMapper.map(note, ConsultationNoteDTO.class);
    }

    @Override
    public ConsultationNoteDTO getConsultationNoteById(Long noteId) {
         ConsultationNote note = consultationNoteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("ConsultationNote", "id", noteId));
        return modelMapper.map(note, ConsultationNoteDTO.class);
    }


    @Override
    @Transactional
    public void deleteConsultationNoteByEncounterId(Long encounterId) {
        ConsultationNote note = consultationNoteRepository.findByEncounterId(encounterId)
                .orElseThrow(() -> new ResourceNotFoundException("ConsultationNote", "encounterId", encounterId));
        consultationNoteRepository.delete(note);
    }
}