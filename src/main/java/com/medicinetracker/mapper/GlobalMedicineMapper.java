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
        GlobalMedicine entity = new GlobalMedicine();
        entity.setName(dto.getName());
        entity.setBrandName(dto.getBrandName());
        entity.setGenericName(dto.getGenericName());
        entity.setDosageForm(dto.getDosageForm());
        entity.setStrength(dto.getStrength());
        entity.setManufacturer(dto.getManufacturer());
        entity.setDescription(dto.getDescription());
        entity.setIndications(dto.getIndications());
        entity.setContraindications(dto.getContraindications());
        entity.setSideEffects(dto.getSideEffects());
        entity.setWarnings(dto.getWarnings());
        entity.setInteractions(dto.getInteractions());
        entity.setStorageInstructions(dto.getStorageInstructions());
        entity.setCategory(dto.getCategory());
        entity.setAtcCode(dto.getAtcCode());
        entity.setFdaApprovalDate(dto.getFdaApprovalDate());
        return entity;
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
