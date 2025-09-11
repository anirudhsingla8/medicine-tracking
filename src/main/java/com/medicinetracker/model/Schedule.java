package com.medicinetracker.model;

import jakarta.persistence.*;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "schedules")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id", nullable = false)
    private UserMedicine medicine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "time_of_day", nullable = false)
    private LocalTime timeOfDay;

    @Column(nullable = false)
    private String frequency;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
    }

    // Constructors
    public Schedule() {}

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UserMedicine getMedicine() { return medicine; }
    public void setMedicine(UserMedicine medicine) { this.medicine = medicine; }
    public Profile getProfile() { return profile; }
    public void setProfile(Profile profile) { this.profile = profile; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public LocalTime getTimeOfDay() { return timeOfDay; }
    public void setTimeOfDay(LocalTime timeOfDay) { this.timeOfDay = timeOfDay; }
    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
