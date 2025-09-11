package com.medicinetracker.dto;

import java.time.LocalTime;
import java.util.UUID;

public class ScheduleResponseDto {
    private UUID id;
    private UUID medicineId;
    private LocalTime timeOfDay;
    private String frequency;
    private boolean isActive;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(UUID medicineId) {
        this.medicineId = medicineId;
    }

    public LocalTime getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(LocalTime timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
