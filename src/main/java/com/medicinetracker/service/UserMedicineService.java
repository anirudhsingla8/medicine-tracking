package com.medicinetracker.service;

import com.medicinetracker.dto.UserMedicineDto;
import com.medicinetracker.exception.ResourceNotFoundException;
import com.medicinetracker.mapper.UserMedicineMapper;
import com.medicinetracker.model.Profile;
import com.medicinetracker.model.User;
import com.medicinetracker.model.UserMedicine;
import com.medicinetracker.repository.ProfileRepository;
import com.medicinetracker.repository.UserMedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserMedicineService {

    private final UserMedicineRepository userMedicineRepository;
    private final ProfileRepository profileRepository;
    private final UserMedicineMapper userMedicineMapper;

    @Transactional
    public UserMedicine createMedicine(UserMedicineDto medicineDto, User currentUser) {
        Profile profile = getProfileAndVerifyOwnership(medicineDto.getProfileId(), currentUser.getId());
        UserMedicine medicine = userMedicineMapper.toEntity(medicineDto, currentUser, profile);
        return userMedicineRepository.save(medicine);
    }

    @Transactional(readOnly = true)
    public List<UserMedicine> getMedicinesForProfile(UUID profileId, User currentUser) {
        getProfileAndVerifyOwnership(profileId, currentUser.getId()); // Verify access
        return userMedicineRepository.findAllByProfileId(profileId);
    }

    @Transactional(readOnly = true)
    public UserMedicine getMedicineById(UUID medicineId, User currentUser) {
        UserMedicine medicine = findMedicineById(medicineId);
        verifyMedicineOwnership(medicine, currentUser.getId());
        return medicine;
    }

    @Transactional
    public UserMedicine updateMedicine(UUID medicineId, UserMedicineDto medicineDto, User currentUser) {
        UserMedicine medicine = findMedicineById(medicineId);
        verifyMedicineOwnership(medicine, currentUser.getId());

        // Ensure the profile is not being changed to one the user doesn't own
        if (!medicine.getProfile().getId().equals(medicineDto.getProfileId())) {
             getProfileAndVerifyOwnership(medicineDto.getProfileId(), currentUser.getId());
        }

        // Update fields
        medicine.setName(medicineDto.getName());
        medicine.setDosage(medicineDto.getDosage());
        medicine.setQuantity(medicineDto.getQuantity());
        medicine.setExpiryDate(medicineDto.getExpiryDate());
        medicine.setCategory(medicineDto.getCategory());
        medicine.setNotes(medicineDto.getNotes());
        medicine.setComposition(medicineDto.getComposition());
        medicine.setForm(medicineDto.getForm());
        medicine.setImageUrl(medicineDto.getImageUrl());
        // profileId is not updated here, as it's a more complex operation

        return userMedicineRepository.save(medicine);
    }

    @Transactional
    public void deleteMedicine(UUID medicineId, User currentUser) {
        UserMedicine medicine = findMedicineById(medicineId);
        verifyMedicineOwnership(medicine, currentUser.getId());
        userMedicineRepository.delete(medicine); // This will be a soft delete due to @SQLDelete
    }

    @Transactional
    public UserMedicine takeDose(UUID medicineId, User currentUser) {
        UserMedicine medicine = findMedicineById(medicineId);
        verifyMedicineOwnership(medicine, currentUser.getId());

        if (medicine.getQuantity() <= 0) {
            throw new IllegalStateException("Medicine quantity is already 0");
        }

        medicine.setQuantity(medicine.getQuantity() - 1);
        return userMedicineRepository.save(medicine);
    }

    @Transactional
    public UserMedicine updateMedicineImage(UUID medicineId, String imageUrl, User currentUser) {
        UserMedicine medicine = findMedicineById(medicineId);
        verifyMedicineOwnership(medicine, currentUser.getId());
        medicine.setImageUrl(imageUrl);
        return userMedicineRepository.save(medicine);
    }

    private Profile getProfileAndVerifyOwnership(UUID profileId, UUID userId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with id: " + profileId));
        if (!profile.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You are not authorized to access this profile.");
        }
        return profile;
    }

    private UserMedicine findMedicineById(UUID medicineId) {
        return userMedicineRepository.findById(medicineId)
            .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + medicineId));
    }

    private void verifyMedicineOwnership(UserMedicine medicine, UUID userId) {
        if (!medicine.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You are not authorized to access this medicine.");
        }
    }
}
