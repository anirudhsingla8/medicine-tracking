package com.medicinetracker.service;

import com.medicinetracker.config.JwtTokenProvider;
import com.medicinetracker.dto.ForgotPasswordDto;
import com.medicinetracker.dto.LoginRequestDto;
import com.medicinetracker.dto.UserRegistrationDto;
import com.medicinetracker.exception.ResourceNotFoundException;
import com.medicinetracker.model.User;
import com.medicinetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public void registerUser(UserRegistrationDto registrationDto) {
        if (userRepository.findByEmail(registrationDto.getEmail()).isPresent()) {
            throw new IllegalStateException("User with email " + registrationDto.getEmail() + " already exists.");
        }

        User user = new User();
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        userRepository.save(user);
    }

    public String login(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        return jwtTokenProvider.generateToken(user);
    }

    @Transactional
    public void forgotPassword(ForgotPasswordDto forgotPasswordDto) {
        User user = userRepository.findByEmail(forgotPasswordDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + forgotPasswordDto.getEmail()));

        user.setPassword(passwordEncoder.encode(forgotPasswordDto.getNewPassword()));
        user.setPasswordLastChanged(OffsetDateTime.now());
        userRepository.save(user);
    }
}
