package com.medicinetracker.mapper;

import com.medicinetracker.dto.ScheduleDto;
import com.medicinetracker.dto.ScheduleResponseDto;
import com.medicinetracker.model.Profile;
import com.medicinetracker.model.Schedule;
import com.medicinetracker.model.User;
import com.medicinetracker.model.UserMedicine;
import org.springframework.stereotype.Component;

@Component
public class ScheduleMapper {

    public Schedule toEntity(ScheduleDto dto, UserMedicine medicine, Profile profile, User user) {
        if (dto == null) {
            return null;
        }
        return Schedule.builder()
                .medicine(medicine)
                .profile(profile)
                .user(user)
                .timeOfDay(dto.getTimeOfDay())
                .frequency(dto.getFrequency())
                .isActive(dto.getIsActive())
                .build();
    }

    public ScheduleResponseDto toResponseDto(Schedule entity) {
        if (entity == null) {
            return null;
        }
        ScheduleResponseDto dto = new ScheduleResponseDto();
        dto.setId(entity.getId());
        dto.setMedicineId(entity.getMedicine().getId());
        dto.setTimeOfDay(entity.getTimeOfDay());
        dto.setFrequency(entity.getFrequency());
        dto.setActive(entity.isActive());
        return dto;
    }
}
