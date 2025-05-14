package com.hospitalmanagement.hms_backend.repository;

import com.hospitalmanagement.hms_backend.entity.MedicalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Long> {
    List<MedicalHistory> findByPatientId(Long patientId);
    List<MedicalHistory> findByPatientIdAndHistoryType(Long patientId, String historyType);
}