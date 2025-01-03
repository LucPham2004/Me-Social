package com.me_social.MeSocial.entity.modal;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.me_social.MeSocial.enums.GroupPrivacy;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "`social_groups`")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String location;

    @Column(name = "image_url")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private GroupPrivacy privacy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinTable( name = "groups_admins", 
                joinColumns = @JoinColumn(name = "group_id"), 
                inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonManagedReference(value = "groups_admins")
    private Set<User> admins = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinTable( name = "groups_members", 
                joinColumns = @JoinColumn(name = "group_id"), 
                inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonManagedReference(value = "groups_members")
	private Set<User> members = new HashSet<>();
    
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "group_posts")
    private Set<Post> posts;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
