package com.medicinetracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
public class ScheduleDto {

    @NotNull(message = "Time of day is required")
    private LocalTime timeOfDay;

    @NotBlank(message = "Frequency is required")
    private String frequency;

    @NotNull(message = "Active status is required")
    private Boolean isActive;
}
