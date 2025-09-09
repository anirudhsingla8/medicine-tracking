package com.medicinetracker.service;

import com.medicinetracker.dto.ScheduleDto;
import com.medicinetracker.exception.ResourceNotFoundException;
import com.medicinetracker.mapper.ScheduleMapper;
import com.medicinetracker.model.Schedule;
import com.medicinetracker.model.User;
import com.medicinetracker.model.UserMedicine;
import com.medicinetracker.repository.ScheduleRepository;
import com.medicinetracker.repository.UserMedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserMedicineRepository userMedicineRepository;
    private final ScheduleMapper scheduleMapper;

    @Transactional
    public Schedule createSchedule(UUID medicineId, ScheduleDto scheduleDto, User currentUser) {
        UserMedicine medicine = getMedicineAndVerifyOwnership(medicineId, currentUser.getId());
        Schedule schedule = scheduleMapper.toEntity(scheduleDto, medicine, medicine.getProfile(), currentUser);
        return scheduleRepository.save(schedule);
    }

    @Transactional(readOnly = true)
    public List<Schedule> getSchedulesForMedicine(UUID medicineId, User currentUser) {
        getMedicineAndVerifyOwnership(medicineId, currentUser.getId()); // Verify access
        return scheduleRepository.findAllByMedicineId(medicineId);
    }

    @Transactional
    public Schedule updateSchedule(UUID scheduleId, ScheduleDto scheduleDto, User currentUser) {
        Schedule schedule = getScheduleAndVerifyOwnership(scheduleId, currentUser.getId());

        schedule.setTimeOfDay(scheduleDto.getTimeOfDay());
        schedule.setFrequency(scheduleDto.getFrequency());
        schedule.setActive(scheduleDto.getIsActive());

        return scheduleRepository.save(schedule);
    }

    @Transactional
    public void deleteSchedule(UUID scheduleId, User currentUser) {
        Schedule schedule = getScheduleAndVerifyOwnership(scheduleId, currentUser.getId());
        scheduleRepository.delete(schedule);
    }

    private UserMedicine getMedicineAndVerifyOwnership(UUID medicineId, UUID userId) {
        UserMedicine medicine = userMedicineRepository.findById(medicineId)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + medicineId));
        if (!medicine.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You are not authorized to access this medicine.");
        }
        return medicine;
    }

    private Schedule getScheduleAndVerifyOwnership(UUID scheduleId, UUID userId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with id: " + scheduleId));
        if (!schedule.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You are not authorized to access this schedule.");
        }
        return schedule;
    }
}
