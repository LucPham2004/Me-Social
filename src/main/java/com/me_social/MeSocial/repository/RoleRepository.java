package com.me_social.MeSocial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.me_social.MeSocial.entity.modal.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
}
