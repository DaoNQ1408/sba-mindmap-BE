package com.sbaproject.sbamindmap.entity;

import com.sbaproject.sbamindmap.enums.SharedStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "collections")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Collection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "collection_id")
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
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "collection", cascade = CascadeType.ALL)
    private List<Mindmap> mindmaps;

    @PrePersist
    public void prePersist() {
        if(name == null || name.isEmpty()) {
            name = "New Collection";
        }
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
}
