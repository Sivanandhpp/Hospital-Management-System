package com.hospitalmanagement.hms_backend.repository;

import com.hospitalmanagement.hms_backend.entity.ConsultationNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConsultationNoteRepository extends JpaRepository<ConsultationNote, Long> {
    Optional<ConsultationNote> findByEncounterId(Long encounterId);
}