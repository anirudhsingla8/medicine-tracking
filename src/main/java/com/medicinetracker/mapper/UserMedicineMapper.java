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
        return UserMedicine.builder()
                .name(dto.getName())
                .user(user)
                .profile(profile)
                .dosage(dto.getDosage())
                .quantity(dto.getQuantity())
                .expiryDate(dto.getExpiryDate())
                .category(dto.getCategory())
                .notes(dto.getNotes())
                .imageUrl(dto.getImageUrl())
                .composition(dto.getComposition())
                .form(dto.getForm())
                .build();
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
