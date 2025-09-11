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
        Schedule entity = new Schedule();
        entity.setMedicine(medicine);
        entity.setProfile(profile);
        entity.setUser(user);
        entity.setTimeOfDay(dto.getTimeOfDay());
        entity.setFrequency(dto.getFrequency());
        entity.setActive(dto.getIsActive());
        return entity;
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
