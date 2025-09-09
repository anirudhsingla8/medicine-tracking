package com.medicinetracker.dto;

import com.medicinetracker.model.MedicineComponent;
import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class UserMedicineResponseDto {
    private UUID id;
    private UUID profileId;
    private String name;
    private String dosage;
    private Integer quantity;
    private LocalDate expiryDate;
    private String category;
    private String notes;
    private String imageUrl;
    private List<MedicineComponent> composition;
    private String form;
    private String status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
