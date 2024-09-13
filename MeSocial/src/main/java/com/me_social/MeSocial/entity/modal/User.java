package com.me_social.MeSocial.entity.modal;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
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

    private Date dob; // date of birth

    private String gender;

    private String bio;

    private String location;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

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
    @OneToMany(mappedBy = "admin", fetch = FetchType.LAZY)
    @JsonManagedReference(value = "group_admin_user")
    private Set<Group> adminGroups;

    @ManyToMany(mappedBy = "members")
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
    private Set<Follow> following;

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Follow> follower;

    // Friendships
    @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Friendship> friendRequestsSent;

    @OneToMany(mappedBy = "accepter", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Friendship> friendRequestsReceived;

    // Constructors
    public User(Long id, String username, String email, String password, String firstName, String lastName,
            String phone, Date dob, String gender, String bio, String location, LocalDateTime createdAt,
            LocalDateTime updatedAt, Set<Role> authorities, Set<Post> posts, Set<Like> likes, Set<Comment> comments,
            Set<Group> adminGroups, Set<Group> memberGroups, Set<DirectMessage> sendMessages,
            Set<DirectMessage> receiveMessages, Set<Follow> following, Set<Follow> follower,
            Set<Friendship> friendRequestsSent, Set<Friendship> friendRequestsReceived) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.dob = dob;
        this.gender = gender;
        this.bio = bio;
        this.location = location;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.authorities = authorities;
        this.posts = posts;
        this.likes = likes;
        this.comments = comments;
        this.adminGroups = adminGroups;
        this.memberGroups = memberGroups;
        this.sendMessages = sendMessages;
        this.receiveMessages = receiveMessages;
        this.following = following;
        this.follower = follower;
        this.friendRequestsSent = friendRequestsSent;
        this.friendRequestsReceived = friendRequestsReceived;
    }

    public User() {
    }

    @Override
    public String toString() {
    return "User [id=" + id + ", username=" + username + ", email=" + email + ", password=" + password
            + ", firstName=" + firstName + ", lastName=" + lastName + ", phone=" + phone + ", dob=" + dob
            + ", gender=" + gender + ", bio=" + bio + ", location=" + location + ", createdAt=" + createdAt
            + ", updatedAt=" + updatedAt + ", authorities=" + authorities + ", posts=" + posts + ", likes=" + likes
            + ", comments=" + comments + ", adminGroups=" + adminGroups + ", memberGroups=" + memberGroups
            + ", sendMessages=" + sendMessages + ", receiveMessages=" + receiveMessages + ", following=" + following
            + ", follower=" + follower + ", friendRequestsSent=" + friendRequestsSent + ", friendRequestsReceived="
            + friendRequestsReceived + "]";
    }
    
    // Getters & Setters

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Set<Role> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Role> authorities) {
        this.authorities = authorities;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    public Set<Like> getLikes() {
        return likes;
    }

    public void setLikes(Set<Like> likes) {
        this.likes = likes;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Group> getAdminGroups() {
        return adminGroups;
    }

    public void setAdminGroups(Set<Group> adminGroups) {
        this.adminGroups = adminGroups;
    }

    public Set<Group> getMemberGroups() {
        return memberGroups;
    }

    public void setMemberGroups(Set<Group> memberGroups) {
        this.memberGroups = memberGroups;
    }

    public Set<DirectMessage> getSendMessages() {
        return sendMessages;
    }

    public void setSendMessages(Set<DirectMessage> sendMessages) {
        this.sendMessages = sendMessages;
    }

    public Set<DirectMessage> getReceiveMessages() {
        return receiveMessages;
    }

    public void setReceiveMessages(Set<DirectMessage> receiveMessages) {
        this.receiveMessages = receiveMessages;
    }

    public Set<Follow> getFollowing() {
        return following;
    }

    public void setFollowing(Set<Follow> following) {
        this.following = following;
    }

    public Set<Follow> getFollower() {
        return follower;
    }

    public void setFollower(Set<Follow> follower) {
        this.follower = follower;
    }

    public Set<Friendship> getFriendRequestsSent() {
        return friendRequestsSent;
    }

    public void setFriendRequestsSent(Set<Friendship> friendRequestsSent) {
        this.friendRequestsSent = friendRequestsSent;
    }

    public Set<Friendship> getFriendRequestsReceived() {
        return friendRequestsReceived;
    }

    public void setFriendRequestsReceived(Set<Friendship> friendRequestsReceived) {
        this.friendRequestsReceived = friendRequestsReceived;
    }
}
