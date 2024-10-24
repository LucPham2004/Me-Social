package com.me_social.MeSocial.entity.modal;

import java.util.Set;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tags")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "created_at", nullable = true, updatable = true)
    private Instant createdAt;

    @ManyToMany(mappedBy="tags")
    @JsonBackReference(value = "posts_tags")
	private Set<Post> posts;

    @ManyToMany(mappedBy="tags")
    @JsonBackReference(value = "reels_tags")
	private Set<Reel> reel;
    
    @PrePersist
    public void handleBeforeCreate() {
        this.createdAt = Instant.now();
    }

}