package com.medicinetracker.repository;

import com.medicinetracker.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {
    List<Schedule> findAllByMedicineId(UUID medicineId);

    @Query("SELECT s FROM Schedule s WHERE s.isActive = true AND s.timeOfDay = :time")
    List<Schedule> findAllActiveSchedulesByTime(@Param("time") LocalTime time);
}
