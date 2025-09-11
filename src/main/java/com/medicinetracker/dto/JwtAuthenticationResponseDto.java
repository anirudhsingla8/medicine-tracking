package com.medicinetracker.dto;

public class JwtAuthenticationResponseDto {
    private String token;

    public JwtAuthenticationResponseDto() {
    }

    public JwtAuthenticationResponseDto(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
