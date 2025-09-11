package com.medicinetracker.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserMedicine> medicines;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
    }

    // Constructors
    public Profile() {}

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public List<UserMedicine> getMedicines() { return medicines; }
    public void setMedicines(List<UserMedicine> medicines) { this.medicines = medicines; }
}
