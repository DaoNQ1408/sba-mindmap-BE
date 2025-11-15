package com.sbaproject.sbamindmap.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "generated_datas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class GeneratedData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "generated_data_id")
    private Long generatedDataId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    @JsonIgnoreProperties({"conversation", "generatedData"})
    private Message message;

    @Column(columnDefinition = "TEXT")
    private String nodes;

    @Column(columnDefinition = "TEXT")
    private String edges;

    @Column(name = "knowledge_json", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String knowledgeJson;

    @Column(name = "is_checked")
    private Boolean isChecked = false;

    @Column(name = "created_at")
    private Instant createdAt;

    @OneToMany(mappedBy = "generatedData", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"generatedData", "user", "template", "collection"})
    private List<Mindmap> mindmaps;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }
}
