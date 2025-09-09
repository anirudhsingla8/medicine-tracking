package com.medicinetracker.repository;

import com.medicinetracker.model.UserMedicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserMedicineRepository extends JpaRepository<UserMedicine, UUID> {
    List<UserMedicine> findAllByProfileId(UUID profileId);

    @Query("SELECT m FROM UserMedicine m WHERE m.quantity < :threshold AND m.quantity >= 0")
    List<UserMedicine> findLowStockMedicines(@Param("threshold") int threshold);

    @Query("SELECT m FROM UserMedicine m WHERE m.expiryDate >= :today AND m.expiryDate <= :expiryLimit")
    List<UserMedicine> findExpiringMedicines(@Param("today") LocalDate today, @Param("expiryLimit") LocalDate expiryLimit);
}
