package com.hospitalmanagement.hms_backend.config;

import com.hospitalmanagement.hms_backend.security.JwtAuthenticationEntryPoint;
import com.hospitalmanagement.hms_backend.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // Make sure this is imported
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
// Remove if not used: import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// Import for CORS
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.List;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // UserDetailsService is injected into JwtAuthenticationFilter if needed there,
    // or directly if you configure AuthenticationManagerBuilder manually.
    // For AuthenticationConfiguration, it's often not directly injected here.
    // private final UserDetailsService userDetailsService;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAuthenticationFilter authenticationFilter;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Apply CORS configuration from the corsConfigurationSource bean
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authorize) ->
                        authorize
                                // IMPORTANT: Allow OPTIONS requests globally for preflight
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .requestMatchers("/api/v1/auth/**").permitAll()
                                // ... your other requestMatchers ...
                                .requestMatchers(HttpMethod.POST, "/api/v1/patients/**").hasAnyRole("ADMIN", "RECEPTIONIST")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/patients/**").hasAnyRole("ADMIN", "RECEPTIONIST")
                                .requestMatchers(HttpMethod.GET, "/api/v1/patients/**").hasAnyRole("ADMIN", "RECEPTIONIST", "DOCTOR", "NURSE")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/patients/**").hasRole("ADMIN")
                                .requestMatchers("/api/v1/appointments/**").hasAnyRole("ADMIN", "RECEPTIONIST", "DOCTOR", "NURSE")
                                .requestMatchers("/api/v1/doctors/**").hasRole("ADMIN")
                                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                                // Add rules for other new EMR endpoints as needed, or secure them by default
                                .requestMatchers("/api/v1/encounters/**").hasAnyRole("ADMIN", "DOCTOR", "NURSE")
                                .requestMatchers("/api/v1/medical-history/**").hasAnyRole("ADMIN", "DOCTOR", "NURSE")
                                .requestMatchers("/api/v1/consultation-notes/**").hasAnyRole("ADMIN", "DOCTOR", "NURSE")
                                .requestMatchers("/api/v1/vital-signs/**").hasAnyRole("ADMIN", "DOCTOR", "NURSE")
                                .requestMatchers("/api/v1/diagnoses/**").hasAnyRole("ADMIN", "DOCTOR", "NURSE")
                                .requestMatchers("/api/v1/medications/**").hasAnyRole("ADMIN", "DOCTOR", "NURSE", "RECEPTIONIST") // Catalog lookup
                                .requestMatchers("/api/v1/prescriptions/**").hasAnyRole("ADMIN", "DOCTOR", "NURSE")
                                .requestMatchers("/api/v1/lab-tests/**").hasAnyRole("ADMIN", "DOCTOR", "NURSE", "RECEPTIONIST", "LAB_TECHNICIAN") // Catalog lookup
                                .requestMatchers("/api/v1/lab-orders/**").hasAnyRole("ADMIN", "DOCTOR", "NURSE", "LAB_TECHNICIAN")
                                .requestMatchers("/api/v1/wards/**").hasAnyRole("ADMIN", "RECEPTIONIST", "DOCTOR", "NURSE")
                                .requestMatchers("/api/v1/beds/**").hasAnyRole("ADMIN", "RECEPTIONIST", "DOCTOR", "NURSE")
                                .requestMatchers("/api/v1/admissions/**").hasAnyRole("ADMIN", "RECEPTIONIST", "DOCTOR", "NURSE")
                                .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Define a CorsConfigurationSource bean
    // This is the recommended way to integrate CORS with Spring Security
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); // Your React app's origin
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        configuration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
        configuration.setAllowCredentials(true); // Important for cookies, Authorization headers with credentials
        configuration.setMaxAge(3600L); // How long the results of a preflight request can be cached (in seconds)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/v1/**", configuration); // Apply this to your API base path
        return source;
    }
}