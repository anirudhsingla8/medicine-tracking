package com.medicinetracker.mapper;

import com.medicinetracker.dto.GlobalMedicineDto;
import com.medicinetracker.dto.GlobalMedicineResponseDto;
import com.medicinetracker.model.GlobalMedicine;
import org.springframework.stereotype.Component;

@Component
public class GlobalMedicineMapper {

    public GlobalMedicine toEntity(GlobalMedicineDto dto) {
        if (dto == null) {
            return null;
        }
        return GlobalMedicine.builder()
                .name(dto.getName())
                .brandName(dto.getBrandName())
                .genericName(dto.getGenericName())
                .dosageForm(dto.getDosageForm())
                .strength(dto.getStrength())
                .manufacturer(dto.getManufacturer())
                .description(dto.getDescription())
                .indications(dto.getIndications())
                .contraindications(dto.getContraindications())
                .sideEffects(dto.getSideEffects())
                .warnings(dto.getWarnings())
                .interactions(dto.getInteractions())
                .storageInstructions(dto.getStorageInstructions())
                .category(dto.getCategory())
                .atcCode(dto.getAtcCode())
                .fdaApprovalDate(dto.getFdaApprovalDate())
                .build();
    }

    public GlobalMedicineResponseDto toResponseDto(GlobalMedicine entity) {
        if (entity == null) {
            return null;
        }
        GlobalMedicineResponseDto dto = new GlobalMedicineResponseDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setBrandName(entity.getBrandName());
        dto.setGenericName(entity.getGenericName());
        dto.setDosageForm(entity.getDosageForm());
        dto.setStrength(entity.getStrength());
        dto.setManufacturer(entity.getManufacturer());
        dto.setDescription(entity.getDescription());
        dto.setIndications(entity.getIndications());
        dto.setContraindications(entity.getContraindications());
        dto.setSideEffects(entity.getSideEffects());
        dto.setWarnings(entity.getWarnings());
        dto.setInteractions(entity.getInteractions());
        dto.setStorageInstructions(entity.getStorageInstructions());
        dto.setCategory(entity.getCategory());
        dto.setAtcCode(entity.getAtcCode());
        dto.setFdaApprovalDate(entity.getFdaApprovalDate());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
