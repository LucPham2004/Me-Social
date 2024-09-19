package com.me_social.MeSocial.entity.modal;

import java.time.Instant;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.me_social.MeSocial.enums.Gender;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String phone;

    private Instant dob; // date of birth

    private Gender gender;

    private String bio;

    private String location;

    @Column(name = "created_at", nullable = true, updatable = true)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    // Entity Relationships

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinTable( name = "user_role", 
                joinColumns = @JoinColumn(name = "user_id"), 
                inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> authorities;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "user_posts")
    private Set<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "user_likes")
    private Set<Like> likes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "user_comments")
    private Set<Comment> comments;

    // Groups
    @ManyToMany(mappedBy = "admins")
    @JsonBackReference(value = "groups_admins")
    private Set<Group> adminGroups;

    @ManyToMany(mappedBy = "members")
    @JsonBackReference(value = "groups_members")
    private Set<Group> memberGroups;

    // Direct Messages
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "messages_sender")
    private Set<DirectMessage> sendMessages;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "messages_receiver")
    private Set<DirectMessage> receiveMessages;

    // Follows
    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Follow> followings;

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Follow> followers;

    // Friendships
    @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Friendship> friendRequestSent;

    @OneToMany(mappedBy = "requestReceiver", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Friendship> friendRequestReceived;

    // @PrePersist
    // public void handleBeforeCreate() {
    //     this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
    //             ? SecurityUtil.getCurrentUserLogin().get()
    //             : "";

    //     this.createdAt = Instant.now();
    // }

}
