package com.medicinetracker.dto;

import com.medicinetracker.model.MedicineComponent;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class UserMedicineDto {

    @NotBlank(message = "Medicine name is required")
    private String name;

    @NotNull(message = "Profile ID is required")
    private UUID profileId;

    private String dosage;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    @NotNull(message = "Expiry date is required")
    @FutureOrPresent(message = "Expiry date must be in the present or future")
    private LocalDate expiryDate;

    private String category;
    private String notes;
    private String imageUrl;
    private List<MedicineComponent> composition;
    private String form;
}
