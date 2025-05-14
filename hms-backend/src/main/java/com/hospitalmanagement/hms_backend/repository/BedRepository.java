package com.hospitalmanagement.hms_backend.repository;

import com.hospitalmanagement.hms_backend.entity.Bed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BedRepository extends JpaRepository<Bed, Long> {
    Optional<Bed> findByWardIdAndBedNumber(Integer wardId, String bedNumber);
    List<Bed> findByWardId(Integer wardId);
    List<Bed> findByIsOccupied(boolean isOccupied);

    @Query("SELECT b FROM Bed b WHERE b.ward.id = :wardId AND b.isOccupied = false")
    List<Bed> findAvailableBedsByWard(@Param("wardId") Integer wardId);
}