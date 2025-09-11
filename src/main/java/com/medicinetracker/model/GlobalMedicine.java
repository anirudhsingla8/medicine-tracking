package com.medicinetracker.model;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "global_medicines")
public class GlobalMedicine {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "brand_name")
    private String brandName;

    @Column(name = "generic_name")
    private String genericName;

    @Column(name = "dosage_form")
    private String dosageForm;

    private String strength;
    private String manufacturer;
    private String description;

    @Type(ListArrayType.class)
    @Column(columnDefinition = "text[]")
    private List<String> indications;

    @Type(ListArrayType.class)
    @Column(columnDefinition = "text[]")
    private List<String> contraindications;

    @Type(ListArrayType.class)
    @Column(columnDefinition = "text[]")
    private List<String> sideEffects;

    @Type(ListArrayType.class)
    @Column(columnDefinition = "text[]")
    private List<String> warnings;

    @Type(ListArrayType.class)
    @Column(columnDefinition = "text[]")
    private List<String> interactions;

    @Column(name = "storage_instructions")
    private String storageInstructions;

    private String category;

    @Column(name = "atc_code")
    private String atcCode;

    @Column(name = "fda_approval_date")
    private LocalDate fdaApprovalDate;

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
        updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }

    // Constructors
    public GlobalMedicine() {}

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }
    public String getGenericName() { return genericName; }
    public void setGenericName(String genericName) { this.genericName = genericName; }
    public String getDosageForm() { return dosageForm; }
    public void setDosageForm(String dosageForm) { this.dosageForm = dosageForm; }
    public String getStrength() { return strength; }
    public void setStrength(String strength) { this.strength = strength; }
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<String> getIndications() { return indications; }
    public void setIndications(List<String> indications) { this.indications = indications; }
    public List<String> getContraindications() { return contraindications; }
    public void setContraindications(List<String> contraindications) { this.contraindications = contraindications; }
    public List<String> getSideEffects() { return sideEffects; }
    public void setSideEffects(List<String> sideEffects) { this.sideEffects = sideEffects; }
    public List<String> getWarnings() { return warnings; }
    public void setWarnings(List<String> warnings) { this.warnings = warnings; }
    public List<String> getInteractions() { return interactions; }
    public void setInteractions(List<String> interactions) { this.interactions = interactions; }
    public String getStorageInstructions() { return storageInstructions; }
    public void setStorageInstructions(String storageInstructions) { this.storageInstructions = storageInstructions; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getAtcCode() { return atcCode; }
    public void setAtcCode(String atcCode) { this.atcCode = atcCode; }
    public LocalDate getFdaApprovalDate() { return fdaApprovalDate; }
    public void setFdaApprovalDate(LocalDate fdaApprovalDate) { this.fdaApprovalDate = fdaApprovalDate; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
