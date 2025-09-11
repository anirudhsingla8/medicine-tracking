package com.medicinetracker.service;

import com.medicinetracker.dto.ProfileDto;
import com.medicinetracker.exception.ResourceNotFoundException;
import com.medicinetracker.model.Profile;
import com.medicinetracker.model.User;
import com.medicinetracker.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    @Transactional
    public Profile createProfile(ProfileDto profileDto, User currentUser) {
        Profile profile = new Profile();
        profile.setName(profileDto.getName());
        profile.setUser(currentUser);
        return profileRepository.save(profile);
    }

    @Transactional(readOnly = true)
    public List<Profile> getProfilesForUser(User currentUser) {
        return profileRepository.findAllByUserId(currentUser.getId());
    }

    @Transactional
    public Profile updateProfile(UUID profileId, ProfileDto profileDto, User currentUser) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with id: " + profileId));

        if (!profile.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not authorized to update this profile.");
        }

        profile.setName(profileDto.getName());
        return profileRepository.save(profile);
    }

    @Transactional
    public void deleteProfile(UUID profileId, User currentUser) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with id: " + profileId));

        if (!profile.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not authorized to delete this profile.");
        }

        profileRepository.delete(profile);
    }
}
