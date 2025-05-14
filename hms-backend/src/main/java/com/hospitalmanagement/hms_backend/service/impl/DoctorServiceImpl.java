package com.hospitalmanagement.hms_backend.service.impl;

import com.hospitalmanagement.hms_backend.dto.DoctorDTO;
import com.hospitalmanagement.hms_backend.entity.Doctor;
import com.hospitalmanagement.hms_backend.entity.User;
import com.hospitalmanagement.hms_backend.exception.HospitalAPIException;
import com.hospitalmanagement.hms_backend.exception.ResourceNotFoundException;
import com.hospitalmanagement.hms_backend.repository.DoctorRepository;
import com.hospitalmanagement.hms_backend.repository.UserRepository;
import com.hospitalmanagement.hms_backend.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public DoctorDTO createDoctor(DoctorDTO doctorDTO) {
        User user = userRepository.findById(doctorDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", doctorDTO.getUserId()));

        // Check if a doctor profile already exists for this user
        if (doctorRepository.findByUser(user).isPresent()) {
            throw new HospitalAPIException(HttpStatus.BAD_REQUEST, "Doctor profile already exists for user ID: " + user.getId());
        }

        // Ensure the user has a DOCTOR role (can be added here or assumed to be pre-assigned)
        // boolean hasDoctorRole = user.getRoles().stream().anyMatch(role -> role.getRoleName().equals(RoleName.ROLE_DOCTOR));
        // if (!hasDoctorRole) {
        //     throw new HospitalAPIException(HttpStatus.BAD_REQUEST, "User does not have ROLE_DOCTOR.");
        // }


        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setSpecialization(doctorDTO.getSpecialization());
        doctor.setLicenseNumber(doctorDTO.getLicenseNumber());
        doctor.setPhoneNumber(doctorDTO.getPhoneNumber());

        Doctor savedDoctor = doctorRepository.save(doctor);
        return mapToDTO(savedDoctor);
    }

    @Override
    public DoctorDTO getDoctorById(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", doctorId));
        return mapToDTO(doctor);
    }

    @Override
    public DoctorDTO getDoctorByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Doctor doctor = doctorRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found for user ID: " + userId));
        return mapToDTO(doctor);
    }

    @Override
    public List<DoctorDTO> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DoctorDTO updateDoctor(Long doctorId, DoctorDTO doctorDTO) {
        Doctor existingDoctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", doctorId));

        // User associated with the doctor profile should not change via this update
        // If doctorDTO.getUserId() is different, it should be handled carefully or disallowed.

        existingDoctor.setSpecialization(doctorDTO.getSpecialization());
        existingDoctor.setLicenseNumber(doctorDTO.getLicenseNumber());
        existingDoctor.setPhoneNumber(doctorDTO.getPhoneNumber());

        Doctor updatedDoctor = doctorRepository.save(existingDoctor);
        return mapToDTO(updatedDoctor);
    }

    @Override
    @Transactional
    public void deleteDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", doctorId));
        // Consider implications: active appointments, admissions, etc.
        // Deleting the Doctor entity might be sufficient if user account is managed separately.
        // If User should also be deactivated/deleted, add logic here or in UserService.
        doctorRepository.delete(doctor);
    }

    @Override
    public List<DoctorDTO> findDoctorsBySpecialization(String specialization) {
        // Assuming DoctorRepository has a method like: List<Doctor> findBySpecializationContainingIgnoreCase(String specialization);
        // For now, filtering in memory:
        return doctorRepository.findAll().stream()
                .filter(doc -> doc.getSpecialization() != null && doc.getSpecialization().equalsIgnoreCase(specialization))
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private DoctorDTO mapToDTO(Doctor doctor) {
        DoctorDTO dto = modelMapper.map(doctor, DoctorDTO.class);
        if (doctor.getUser() != null) {
            dto.setUserId(doctor.getUser().getId());
            dto.setUsername(doctor.getUser().getUsername());
            dto.setFullName(doctor.getUser().getFullName());
            dto.setEmail(doctor.getUser().getEmail());
        }
        return dto;
    }
}