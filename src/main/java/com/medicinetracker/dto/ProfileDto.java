package com.medicinetracker.dto;

import jakarta.validation.constraints.NotBlank;

public class ProfileDto {
    @NotBlank(message = "Profile name is required")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
