package com.hospitalmanagement.hms_backend.repository;

import com.hospitalmanagement.hms_backend.entity.LabTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LabTestRepository extends JpaRepository<LabTest, Long> {
    Optional<LabTest> findByTestName(String testName);
    List<LabTest> findByTestNameContainingIgnoreCase(String name);
    List<LabTest> findByCategory(String category);
}