package com.sbaproject.sbamindmap.entity;

import com.sbaproject.sbamindmap.pojo.template.StyleConfig;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "templates")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_id")
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private StyleConfig styleConfig;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(mappedBy = "templates") // ánh xạ từ User
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL)
    private List<Mindmap> mindmaps;

    @ManyToMany
    @JoinTable(
            name = "template_orders",
            joinColumns = @JoinColumn(name = "template_id"),
            inverseJoinColumns = @JoinColumn(name = "order_id")
    )
    private Set<Packages> packages;

    @PrePersist
    public void prePersist() {
        if(name == null || name.isEmpty()) {
            name = "Untitled Template";
        }
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
}
