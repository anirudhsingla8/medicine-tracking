package com.medicinetracker.controller;

import com.medicinetracker.dto.ScheduleDto;
import com.medicinetracker.dto.ScheduleResponseDto;
import com.medicinetracker.mapper.ScheduleMapper;
import com.medicinetracker.model.Schedule;
import com.medicinetracker.model.User;
import com.medicinetracker.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final ScheduleMapper scheduleMapper;

    @PutMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(
            @PathVariable UUID scheduleId,
            @Valid @RequestBody ScheduleDto scheduleDto,
            @AuthenticationPrincipal User currentUser) {
        Schedule updatedSchedule = scheduleService.updateSchedule(scheduleId, scheduleDto, currentUser);
        return ResponseEntity.ok(scheduleMapper.toResponseDto(updatedSchedule));
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable UUID scheduleId,
            @AuthenticationPrincipal User currentUser) {
        scheduleService.deleteSchedule(scheduleId, currentUser);
        return ResponseEntity.noContent().build();
    }
}
