package com.hospitalmanagement.hms_backend.service.impl;

import com.hospitalmanagement.hms_backend.dto.AuthDTO;
import com.hospitalmanagement.hms_backend.entity.Role;
import com.hospitalmanagement.hms_backend.entity.User;
import com.hospitalmanagement.hms_backend.enums.RoleName;
import com.hospitalmanagement.hms_backend.exception.HospitalAPIException;
import com.hospitalmanagement.hms_backend.repository.RoleRepository;
import com.hospitalmanagement.hms_backend.repository.UserRepository;
import com.hospitalmanagement.hms_backend.security.JwtTokenProvider;
import com.hospitalmanagement.hms_backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public AuthDTO.JWTAuthResponse login(AuthDTO.LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);
        String username = authentication.getName(); // This will be the username
        Set<String> roles = authentication.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toSet());


        return new AuthDTO.JWTAuthResponse(token, "Bearer", username, roles);
    }

    @Override
    @Transactional
    public String register(AuthDTO.RegisterRequest registerRequest) {
        // Check if username already exists
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new HospitalAPIException(HttpStatus.BAD_REQUEST, "Username is already taken!");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new HospitalAPIException(HttpStatus.BAD_REQUEST, "Email is already taken!");
        }

        User user = new User();
        user.setFullName(registerRequest.getFullName());
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setActive(true);

        Set<Role> userRoles = new HashSet<>();
        if (registerRequest.getRoles() == null || registerRequest.getRoles().isEmpty()) {
            // Default role if none provided (e.g., for patient self-registration)
             Role patientRole = roleRepository.findByRoleName(RoleName.ROLE_PATIENT)
                    .orElseThrow(() -> new HospitalAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "Default Patient Role not found."));
            userRoles.add(patientRole);
        } else {
            registerRequest.getRoles().forEach(roleStr -> {
                RoleName roleNameEnum;
                try {
                    roleNameEnum = RoleName.valueOf(roleStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new HospitalAPIException(HttpStatus.BAD_REQUEST, "Invalid role: " + roleStr);
                }
                Role role = roleRepository.findByRoleName(roleNameEnum)
                        .orElseThrow(() -> new HospitalAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "Role not found: " + roleStr));
                userRoles.add(role);
            });
        }
        user.setRoles(userRoles);
        userRepository.save(user);

        return "User registered successfully!";
    }

    @Override
    @Transactional
    public String registerPatientUser(AuthDTO.RegisterRequest registerRequest) {
        // This method can be specialized for patient registration, perhaps with fewer fields or default ROLE_PATIENT
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new HospitalAPIException(HttpStatus.BAD_REQUEST, "Username is already taken!");
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new HospitalAPIException(HttpStatus.BAD_REQUEST, "Email is already taken!");
        }

        User user = new User();
        user.setFullName(registerRequest.getFullName());
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setActive(true);

        Role patientRole = roleRepository.findByRoleName(RoleName.ROLE_PATIENT)
                .orElseThrow(() -> new HospitalAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "Patient Role not found."));
        user.setRoles(Set.of(patientRole)); // Assign only ROLE_PATIENT

        userRepository.save(user);
        // Optionally, create a corresponding Patient entity here if patient details are also provided
        return "Patient user registered successfully!";
    }
}