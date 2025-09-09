package com.medicinetracker.dto;

import lombok.Data;

import java.time.LocalTime;
import java.util.UUID;

@Data
public class ScheduleResponseDto {
    private UUID id;
    private UUID medicineId;
    private LocalTime timeOfDay;
    private String frequency;
    private boolean isActive;
}
