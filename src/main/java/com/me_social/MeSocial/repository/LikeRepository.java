package com.me_social.MeSocial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.me_social.MeSocial.entity.modal.Like;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Like findByUserIdAndPostId(Long userId, Long postId);

    Like findByUserIdAndCommentId(Long userId, Long commentId);

    int countByPostId(Long postId);

    int countByCommentId(Long id);

    int countByReelId(String id);

    int countByStoryId(String id);

    int countByUserId(Long id);
    
    @Query("""
            SELECT COUNT(l) FROM Like l
            """)
    int countAll();

    boolean existsByPostIdAndUserId(Long postId, Long userId);
}
