package com.medicinetracker.model;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
}
