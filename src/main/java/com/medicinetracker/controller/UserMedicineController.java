package com.medicinetracker.controller;

import com.medicinetracker.dto.UserMedicineDto;
import com.medicinetracker.dto.UserMedicineResponseDto;
import com.medicinetracker.mapper.UserMedicineMapper;
import com.medicinetracker.model.User;
import com.medicinetracker.model.UserMedicine;
import com.medicinetracker.dto.ScheduleDto;
import com.medicinetracker.dto.ScheduleResponseDto;
import com.medicinetracker.mapper.ScheduleMapper;
import com.medicinetracker.model.Schedule;
import com.medicinetracker.service.CloudinaryService;
import com.medicinetracker.service.ScheduleService;
import com.medicinetracker.service.UserMedicineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/medicines")
@RequiredArgsConstructor
public class UserMedicineController {

    private final UserMedicineService userMedicineService;
    private final CloudinaryService cloudinaryService;
    private final UserMedicineMapper userMedicineMapper;
    private final ScheduleService scheduleService;
    private final ScheduleMapper scheduleMapper;

    @PostMapping("/profiles/{profileId}/medicines")
    public ResponseEntity<UserMedicineResponseDto> createMedicine(
            @PathVariable UUID profileId,
            @Valid @RequestBody UserMedicineDto medicineDto,
            @AuthenticationPrincipal User currentUser) {
        // Ensure the DTO has the correct profileId from the path
        medicineDto.setProfileId(profileId);
        UserMedicine createdMedicine = userMedicineService.createMedicine(medicineDto, currentUser);
        return new ResponseEntity<>(userMedicineMapper.toResponseDto(createdMedicine), HttpStatus.CREATED);
    }

    @GetMapping("/profiles/{profileId}/medicines")
    public ResponseEntity<List<UserMedicineResponseDto>> getMedicinesForProfile(
            @PathVariable UUID profileId,
            @AuthenticationPrincipal User currentUser) {
        List<UserMedicine> medicines = userMedicineService.getMedicinesForProfile(profileId, currentUser);
        List<UserMedicineResponseDto> responseDtos = medicines.stream()
                .map(userMedicineMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/{medicineId}")
    public ResponseEntity<UserMedicineResponseDto> getMedicineById(
            @PathVariable UUID medicineId,
            @AuthenticationPrincipal User currentUser) {
        UserMedicine medicine = userMedicineService.getMedicineById(medicineId, currentUser);
        return ResponseEntity.ok(userMedicineMapper.toResponseDto(medicine));
    }

    @PutMapping("/{medicineId}")
    public ResponseEntity<UserMedicineResponseDto> updateMedicine(
            @PathVariable UUID medicineId,
            @Valid @RequestBody UserMedicineDto medicineDto,
            @AuthenticationPrincipal User currentUser) {
        UserMedicine updatedMedicine = userMedicineService.updateMedicine(medicineId, medicineDto, currentUser);
        return ResponseEntity.ok(userMedicineMapper.toResponseDto(updatedMedicine));
    }

    @DeleteMapping("/{medicineId}")
    public ResponseEntity<Void> deleteMedicine(
            @PathVariable UUID medicineId,
            @AuthenticationPrincipal User currentUser) {
        userMedicineService.deleteMedicine(medicineId, currentUser);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{medicineId}/takedose")
    public ResponseEntity<UserMedicineResponseDto> takeDose(
            @PathVariable UUID medicineId,
            @AuthenticationPrincipal User currentUser) {
        UserMedicine updatedMedicine = userMedicineService.takeDose(medicineId, currentUser);
        return ResponseEntity.ok(userMedicineMapper.toResponseDto(updatedMedicine));
    }

    @PostMapping("/upload-image/{medicineId}")
    public ResponseEntity<?> uploadImage(
            @PathVariable UUID medicineId,
            @RequestParam("medicineImage") MultipartFile file,
            @AuthenticationPrincipal User currentUser) {
        try {
            String imageUrl = cloudinaryService.uploadFile(file, "medicine_tracker");
            userMedicineService.updateMedicineImage(medicineId, imageUrl, currentUser);
            return ResponseEntity.ok().body(Map.of("imageUrl", imageUrl));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
        }
    }

    // --- Schedule Endpoints ---

    @PostMapping("/{medicineId}/schedules")
    public ResponseEntity<ScheduleResponseDto> createSchedule(
            @PathVariable UUID medicineId,
            @Valid @RequestBody ScheduleDto scheduleDto,
            @AuthenticationPrincipal User currentUser) {
        Schedule createdSchedule = scheduleService.createSchedule(medicineId, scheduleDto, currentUser);
        return new ResponseEntity<>(scheduleMapper.toResponseDto(createdSchedule), HttpStatus.CREATED);
    }

    @GetMapping("/{medicineId}/schedules")
    public ResponseEntity<List<ScheduleResponseDto>> getSchedulesForMedicine(
            @PathVariable UUID medicineId,
            @AuthenticationPrincipal User currentUser) {
        List<Schedule> schedules = scheduleService.getSchedulesForMedicine(medicineId, currentUser);
        List<ScheduleResponseDto> responseDtos = schedules.stream()
                .map(scheduleMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }
}
