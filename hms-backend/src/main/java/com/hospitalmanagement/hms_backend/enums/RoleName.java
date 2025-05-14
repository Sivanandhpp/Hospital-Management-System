package com.hospitalmanagement.hms_backend.enums;

// Matches the `role_name` in the `roles` table.
public enum RoleName {
    ROLE_ADMIN,
    ROLE_DOCTOR,
    ROLE_RECEPTIONIST,
    ROLE_NURSE,
    ROLE_LAB_TECHNICIAN,
    ROLE_PATIENT // If patients can log in
}