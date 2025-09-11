package com.medicinetracker.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String password;

    @Column(name = "password_last_changed")
    private OffsetDateTime passwordLastChanged;

    @Column(name = "fcm_token")
    private String fcmToken;

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Profile> profiles;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
        passwordLastChanged = OffsetDateTime.now();
    }

    // Constructors
    public User() {}

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public OffsetDateTime getPasswordLastChanged() { return passwordLastChanged; }
    public void setPasswordLastChanged(OffsetDateTime passwordLastChanged) { this.passwordLastChanged = passwordLastChanged; }
    public String getFcmToken() { return fcmToken; }
    public void setFcmToken(String fcmToken) { this.fcmToken = fcmToken; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public List<Profile> getProfiles() { return profiles; }
    public void setProfiles(List<Profile> profiles) { this.profiles = profiles; }
    public void setPassword(String password) { this.password = password; }


    // UserDetails implementation
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // No roles implemented for now
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
