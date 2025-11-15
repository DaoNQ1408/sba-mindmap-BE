package com.sbaproject.sbamindmap.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sbaproject.sbamindmap.enums.UserRole;
import com.sbaproject.sbamindmap.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
//import org.hibernate.usertype.UserType;
//import org.w3c.dom.stylesheets.LinkStyle;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "mail", length = 150, unique = true, nullable = false)
    private String mail;

    @Column(name = "full_name", length = 100, nullable = false)
    private String fullName;

    @Column(name = "username")
    private String username;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "user_status")
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Collection> collections;

    @ManyToMany
    @JoinTable(
            name = "user_templates",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "template_id")
    )
    @JsonIgnore
    private List<Template> templates = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<ApiKey> apiKeys;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Orders> orders;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Wallet wallet;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
}
