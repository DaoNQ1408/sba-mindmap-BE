package com.sbaproject.sbamindmap.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "api_keys")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "api_key_id")
    private Long apiKeyId;

    @ManyToOne()
    @JoinColumn(name = "package_id")
    @JsonIgnore
    private Packages aPackage;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "key_value", unique = true, nullable = false)
    private String keyValue;

    @Column(name = "remaining_calls")
    private Integer remainingCalls;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "activated_at")
    private Instant activatedAt;

    @Column(name = "expired_at")
    private Instant expiredAt;
}
