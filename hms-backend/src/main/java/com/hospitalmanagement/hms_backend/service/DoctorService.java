package com.hospitalmanagement.hms_backend.service;

import com.hospitalmanagement.hms_backend.dto.DoctorDTO;

import java.util.List;

public interface DoctorService {
    DoctorDTO createDoctor(DoctorDTO doctorDTO); // Assumes User already exists or is created as part of this
    DoctorDTO getDoctorById(Long doctorId); // Doctor's specific ID
    DoctorDTO getDoctorByUserId(Long userId);
    List<DoctorDTO> getAllDoctors();
    DoctorDTO updateDoctor(Long doctorId, DoctorDTO doctorDTO);
    void deleteDoctor(Long doctorId); // Might also need to handle associated User
    List<DoctorDTO> findDoctorsBySpecialization(String specialization);
}