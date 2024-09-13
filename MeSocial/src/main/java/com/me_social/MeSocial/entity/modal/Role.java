package com.me_social.MeSocial.entity.modal;


import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String authority;

    @ManyToMany(mappedBy = "authorities", fetch = FetchType.LAZY)
    private Set<User> user;
    
    // Constructors

    public Role(String authority) {
        this.authority = authority;
    }

    public Role(Long id, String authority, Set<User> user) {
        this.id = id;
        this.authority = authority;
        this.user = user;
    }

    public Role() {
    }

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Set<User> getUser() {
        return user;
    }

    public void setUser(Set<User> user) {
        this.user = user;
    }
}
