package com.medicinetracker.mapper;

import com.medicinetracker.dto.ProfileResponseDto;
import com.medicinetracker.model.Profile;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {
    public ProfileResponseDto toProfileResponseDto(Profile profile) {
        if (profile == null) {
            return null;
        }
        ProfileResponseDto dto = new ProfileResponseDto();
        dto.setId(profile.getId());
        dto.setName(profile.getName());
        return dto;
    }
}
