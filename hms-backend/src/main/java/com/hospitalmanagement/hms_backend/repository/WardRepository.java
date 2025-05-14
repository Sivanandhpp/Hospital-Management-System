package com.hospitalmanagement.hms_backend.repository;

import com.hospitalmanagement.hms_backend.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WardRepository extends JpaRepository<Ward, Integer> {
    Optional<Ward> findByWardName(String wardName);
}