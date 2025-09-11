package com.medicinetracker.dto;

import com.medicinetracker.model.MedicineComponent;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

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

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getProfileId() { return profileId; }
    public void setProfileId(UUID profileId) { this.profileId = profileId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
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
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
