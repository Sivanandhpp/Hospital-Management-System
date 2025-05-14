package com.hospitalmanagement.hms_backend.repository;

import com.hospitalmanagement.hms_backend.entity.Admission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdmissionRepository extends JpaRepository<Admission, Long> {
    List<Admission> findByPatientId(Long patientId);
    List<Admission> findByWardId(Integer wardId);
    Optional<Admission> findByBedIdAndStatus(Long bedId, com.hospitalmanagement.hms_backend.enums.AdmissionStatus status);

    @Query("SELECT a FROM Admission a WHERE a.patient.id = :patientId AND a.status = 'ADMITTED'")
    Optional<Admission> findActiveAdmissionByPatientId(@Param("patientId") Long patientId);
}