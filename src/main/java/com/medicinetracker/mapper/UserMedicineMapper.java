package com.medicinetracker.mapper;

import com.medicinetracker.dto.UserMedicineDto;
import com.medicinetracker.dto.UserMedicineResponseDto;
import com.medicinetracker.model.Profile;
import com.medicinetracker.model.User;
import com.medicinetracker.model.UserMedicine;
import org.springframework.stereotype.Component;

@Component
public class UserMedicineMapper {

    public UserMedicine toEntity(UserMedicineDto dto, User user, Profile profile) {
        if (dto == null) {
            return null;
        }
        UserMedicine entity = new UserMedicine();
        entity.setName(dto.getName());
        entity.setUser(user);
        entity.setProfile(profile);
        entity.setDosage(dto.getDosage());
        entity.setQuantity(dto.getQuantity());
        entity.setExpiryDate(dto.getExpiryDate());
        entity.setCategory(dto.getCategory());
        entity.setNotes(dto.getNotes());
        entity.setImageUrl(dto.getImageUrl());
        entity.setComposition(dto.getComposition());
        entity.setForm(dto.getForm());
        return entity;
    }

    public UserMedicineResponseDto toResponseDto(UserMedicine entity) {
        if (entity == null) {
            return null;
        }
        UserMedicineResponseDto dto = new UserMedicineResponseDto();
        dto.setId(entity.getId());
        dto.setProfileId(entity.getProfile().getId());
        dto.setName(entity.getName());
        dto.setDosage(entity.getDosage());
        dto.setQuantity(entity.getQuantity());
        dto.setExpiryDate(entity.getExpiryDate());
        dto.setCategory(entity.getCategory());
        dto.setNotes(entity.getNotes());
        dto.setImageUrl(entity.getImageUrl());
        dto.setComposition(entity.getComposition());
        dto.setForm(entity.getForm());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
