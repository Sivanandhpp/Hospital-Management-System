package com.hospitalmanagement.hms_backend.repository;

import com.hospitalmanagement.hms_backend.entity.VitalSign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VitalSignRepository extends JpaRepository<VitalSign, Long> {
    List<VitalSign> findByEncounterIdOrderByRecordedAtDesc(Long encounterId);
}