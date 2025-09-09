package com.medicinetracker.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfileDto {
    @NotBlank(message = "Profile name is required")
    private String name;
}
