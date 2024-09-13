package com.me_social.MeSocial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.me_social.MeSocial.entity.modal.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    
}
