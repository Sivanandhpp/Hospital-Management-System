package com.hospitalmanagement.hms_backend.repository;

import com.hospitalmanagement.hms_backend.entity.Role;
import com.hospitalmanagement.hms_backend.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(RoleName roleName);
}