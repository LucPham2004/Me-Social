package com.me_social.MeSocial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.me_social.MeSocial.entity.modal.Like;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {
    
}
