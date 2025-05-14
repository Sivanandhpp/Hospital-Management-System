package com.hospitalmanagement.hms_backend.repository;

import com.hospitalmanagement.hms_backend.entity.Doctor;
import com.hospitalmanagement.hms_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUser(User user);
    Optional<Doctor> findByUserId(Long userId);
}