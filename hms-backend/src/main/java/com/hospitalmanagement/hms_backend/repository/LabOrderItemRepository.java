package com.hospitalmanagement.hms_backend.repository;

import com.hospitalmanagement.hms_backend.entity.LabOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabOrderItemRepository extends JpaRepository<LabOrderItem, Long> {
    // Custom queries if needed
}