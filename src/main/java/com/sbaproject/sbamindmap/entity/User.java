package com.sbaproject.sbamindmap.entity;

import com.sbaproject.sbamindmap.enums.UserRole;
import com.sbaproject.sbamindmap.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.usertype.UserType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.w3c.dom.stylesheets.LinkStyle;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "mail", length = 150, unique = true, nullable = false)
    private String mail;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "username", length = 50, unique = true, nullable = false)
    private String username;

    @Column(name = "full_name", length = 50)
    private String fullName;

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
    private List<Collection> collections;

    @ManyToMany
    @JoinTable(
            name = "user_templates",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "template_id")
    )
    private List<Template> templates = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ApiKey> apiKeys;

    @OneToMany(mappedBy = "user")
    private List<Orders> orders;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @Override
    public java.util.Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.toString()));
    }

    @Override
    public String getUsername() {
        return this.mail;
    }
}
