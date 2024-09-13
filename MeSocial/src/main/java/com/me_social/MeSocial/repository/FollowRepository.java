package com.me_social.MeSocial.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.me_social.MeSocial.entity.modal.Follow;

public interface FollowRepository extends JpaRepository<Follow, Integer> {
    
}
