package com.me_social.MeSocial.entity.modal;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "`social_groups`")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private GroupPrivacy privacy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    @JsonBackReference(value = "group_admin_user")
    private User admin;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinTable( name = "groups_members", 
                joinColumns = @JoinColumn(name = "user_id"), 
                inverseJoinColumns = @JoinColumn(name = "group_id"))
	private Set<User> members;
    
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "group_posts")
    private Set<Post> posts;

    // Constructors
    public Group(Long id, String name, String description, GroupPrivacy privacy, LocalDateTime createdAt,
        LocalDateTime updatedAt, User admin, Set<User> members, Set<Post> posts) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.privacy = privacy;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.admin = admin;
    this.members = members;
    this.posts = posts;
    }

    public Group() {
    }

    @Override
    public String toString() {
    return "Group [id=" + id + ", name=" + name + ", description=" + description + ", privacy=" + privacy
            + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", admin=" + admin + ", members=" + members
            + ", posts=" + posts + "]";
    }

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GroupPrivacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(GroupPrivacy privacy) {
        this.privacy = privacy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public Set<User> getMembers() {
        return members;
    }

    public void setMembers(Set<User> members) {
        this.members = members;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }
}
