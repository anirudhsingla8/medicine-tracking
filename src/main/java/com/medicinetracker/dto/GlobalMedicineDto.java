package com.medicinetracker.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class GlobalMedicineDto {
    @NotBlank(message = "Name is required")
    private String name;

    private String brandName;
    private String genericName;
    private String dosageForm;
    private String strength;
    private String manufacturer;
    private String description;
    private List<String> indications;
    private List<String> contraindications;
    private List<String> sideEffects;
    private List<String> warnings;
    private List<String> interactions;
    private String storageInstructions;
    private String category;
    private String atcCode;
    private LocalDate fdaApprovalDate;
}
