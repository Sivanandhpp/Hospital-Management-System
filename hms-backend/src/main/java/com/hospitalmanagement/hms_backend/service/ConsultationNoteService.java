package com.hospitalmanagement.hms_backend.service;

import com.hospitalmanagement.hms_backend.dto.ConsultationNoteDTO;

public interface ConsultationNoteService {
    ConsultationNoteDTO saveOrUpdateConsultationNote(ConsultationNoteDTO consultationNoteDTO);
    ConsultationNoteDTO getConsultationNoteByEncounterId(Long encounterId);
    ConsultationNoteDTO getConsultationNoteById(Long noteId); // Less common, usually by encounter
    void deleteConsultationNoteByEncounterId(Long encounterId); // If allowed
}