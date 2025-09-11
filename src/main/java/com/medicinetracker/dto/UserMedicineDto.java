package com.medicinetracker.dto;

import com.medicinetracker.model.MedicineComponent;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public UUID getProfileId() { return profileId; }
    public void setProfileId(UUID profileId) { this.profileId = profileId; }
    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public List<MedicineComponent> getComposition() { return composition; }
    public void setComposition(List<MedicineComponent> composition) { this.composition = composition; }
    public String getForm() { return form; }
    public void setForm(String form) { this.form = form; }
}
