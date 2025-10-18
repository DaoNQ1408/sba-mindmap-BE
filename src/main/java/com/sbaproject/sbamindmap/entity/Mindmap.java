package com.sbaproject.sbamindmap.entity;

import com.sbaproject.sbamindmap.enums.SharedStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "mindmaps")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Mindmap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mindmap_id")
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "shared_status")
    @Enumerated(EnumType.STRING)
    private SharedStatus sharedStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "collection_id", nullable = false)
    private Collection collection;

    @ManyToOne
    @JoinColumn(name = "template_id", nullable = false)
    private Template template;

    @PrePersist
    public void prePersist() {
        if(name == null || name.isEmpty()) {
            name = "Untitled Mindmap";
        }
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
}
