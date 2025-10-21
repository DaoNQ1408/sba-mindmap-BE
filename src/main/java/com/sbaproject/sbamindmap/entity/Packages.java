package com.sbaproject.sbamindmap.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "packages")
public class Packages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_id")
    private long packageId;
    @Column(name = "package_name", length = 100)
    private String name;
    @Column(name = "price")
    private double price;
    @Column(name = "api_call_limit")
    private String apiCallLimit;
    @Column(name = "duration_days")
    private LocalDateTime durationDays;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany(mappedBy = "packages")
    private Set<Template> templates;

    @OneToMany(mappedBy = "packages", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Orders> orders;

    @OneToMany(mappedBy = "aPackage")
    private List<ApiKey> apiKeys;

}
