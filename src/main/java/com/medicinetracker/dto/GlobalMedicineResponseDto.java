package com.medicinetracker.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class GlobalMedicineResponseDto {
    private UUID id;
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
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
