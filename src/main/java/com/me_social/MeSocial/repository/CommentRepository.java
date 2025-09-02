package com.me_social.MeSocial.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.me_social.MeSocial.entity.modal.Comment;

@Repository
public interface CommentRepository extends PagingAndSortingRepository<Comment, Long> {
    Comment save(Comment comment);

    void delete(Comment comment);

    Optional<Comment> findById(Long id);

    boolean existsById(Long id);

    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId AND c.parentComment.id IS NULL")
    Page<Comment> findTopLevelCommentsByPostId(@Param("postId") Long postId, Pageable pageable);

    @Query(value = """
            WITH RECURSIVE RecursiveComments AS (
                SELECT c.id, c.content, c.created_at, c.updated_at, c.parent_comment_id, c.post_id, c.reel_id, c.user_id
                FROM comments c
                WHERE c.id = :topCommentId
                UNION ALL
                SELECT child.id, child.content, child.created_at, child.updated_at, child.parent_comment_id, child.post_id, child.reel_id, child.user_id
                FROM comments child
                INNER JOIN RecursiveComments parent
                ON child.parent_comment_id = parent.id
            )
            SELECT * 
            FROM RecursiveComments
            WHERE id != :topCommentId
            """,
            countQuery = """
                    WITH RECURSIVE RecursiveComments AS (
                        SELECT c.id, c.content, c.created_at, c.updated_at, c.parent_comment_id, c.post_id, c.reel_id, c.user_id
                        FROM comments c
                        WHERE c.id = :topCommentId
                        UNION ALL
                        SELECT child.id, child.content, child.created_at, child.updated_at, child.parent_comment_id, child.post_id, child.reel_id, child.user_id
                        FROM comments child
                        INNER JOIN RecursiveComments parent
                        ON child.parent_comment_id = parent.id
                    )
                    SELECT COUNT(*) 
                    FROM RecursiveComments
                    WHERE id != :topCommentId
                    """,
            nativeQuery = true)
    Page<Comment> findAllChildComments(@Param("topCommentId") Long topCommentId, Pageable pageable);


    Page<Comment> findByUserId(Long id, Pageable pageable);

    int countByPostId(Long id);

    int countByUserId(Long id);

    int countByReelId(String id);

    @Query("""
            SELECT COUNT(c) FROM Comment c
            """)
    int countAll();

    @Query(
            value = "SELECT DATE_FORMAT(c.created_at, '%Y-%m') AS month, COUNT(*) AS count " +
                    "FROM comments c " +
                    "GROUP BY DATE_FORMAT(c.created_at, '%Y-%m')",
            nativeQuery = true
    )
    List<Object[]> countCommentsPerMonth();
}
