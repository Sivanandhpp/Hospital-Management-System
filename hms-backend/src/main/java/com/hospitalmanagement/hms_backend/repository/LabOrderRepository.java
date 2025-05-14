package com.hospitalmanagement.hms_backend.repository;

import com.hospitalmanagement.hms_backend.entity.LabOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabOrderRepository extends JpaRepository<LabOrder, Long> {
    List<LabOrder> findByEncounterId(Long encounterId);
    List<LabOrder> findByEncounterPatientId(Long patientId);
}