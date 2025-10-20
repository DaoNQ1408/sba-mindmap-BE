package com.sbaproject.sbamindmap.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "templates")
public class Templates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long templateId;
    @Column(name = "template_name", length = 100)
    private String templateName;
    @Column(name = "created_at", length = 500)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    @ManyToMany
    @JoinTable(
            name = "template_orders",
            joinColumns = @JoinColumn(name = "template_id"),
            inverseJoinColumns = @JoinColumn(name = "order_id")
    )
    private Set<Packages> packages;
}
