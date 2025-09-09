package com.medicinetracker.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ProfileResponseDto {
    private UUID id;
    private String name;
}
