package com.medicinetracker.controller;

import com.medicinetracker.dto.ProfileDto;
import com.medicinetracker.dto.ProfileResponseDto;
import com.medicinetracker.mapper.ProfileMapper;
import com.medicinetracker.model.Profile;
import com.medicinetracker.model.User;
import com.medicinetracker.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final ProfileMapper profileMapper;

    @PostMapping
    public ResponseEntity<ProfileResponseDto> createProfile(
            @Valid @RequestBody ProfileDto profileDto,
            @AuthenticationPrincipal User currentUser) {
        Profile profile = profileService.createProfile(profileDto, currentUser);
        return new ResponseEntity<>(profileMapper.toProfileResponseDto(profile), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProfileResponseDto>> getProfiles(
            @AuthenticationPrincipal User currentUser) {
        List<Profile> profiles = profileService.getProfilesForUser(currentUser);
        List<ProfileResponseDto> responseDtos = profiles.stream()
                .map(profileMapper::toProfileResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @PutMapping("/{profileId}")
    public ResponseEntity<ProfileResponseDto> updateProfile(
            @PathVariable UUID profileId,
            @Valid @RequestBody ProfileDto profileDto,
            @AuthenticationPrincipal User currentUser) {
        Profile updatedProfile = profileService.updateProfile(profileId, profileDto, currentUser);
        return ResponseEntity.ok(profileMapper.toProfileResponseDto(updatedProfile));
    }

    @DeleteMapping("/{profileId}")
    public ResponseEntity<Void> deleteProfile(
            @PathVariable UUID profileId,
            @AuthenticationPrincipal User currentUser) {
        profileService.deleteProfile(profileId, currentUser);
        return ResponseEntity.noContent().build();
    }
}
