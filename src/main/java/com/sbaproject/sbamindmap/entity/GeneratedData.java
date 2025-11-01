package com.sbaproject.sbamindmap.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "generated_datas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneratedData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "generated_data_id")
    private Long generatedDataId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    private Message message;

    @Column(columnDefinition = "TEXT")
    private String nodes; // JSON text

    @Column(columnDefinition = "TEXT")
    private String edges; // JSON text

    @Column(name = "is_checked")
    private Boolean isChecked = false;

    @Column(name = "created_at")
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }
}

