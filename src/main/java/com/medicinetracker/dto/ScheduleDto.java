package com.medicinetracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public class ScheduleDto {

    @NotNull(message = "Time of day is required")
    private LocalTime timeOfDay;

    @NotBlank(message = "Frequency is required")
    private String frequency;

    @NotNull(message = "Active status is required")
    private Boolean isActive;

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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }
}
