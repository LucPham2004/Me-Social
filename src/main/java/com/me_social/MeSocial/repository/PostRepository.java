package com.me_social.MeSocial.repository;

import java.util.List;
import java.util.Optional;

import com.me_social.MeSocial.entity.dto.response.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.me_social.MeSocial.entity.modal.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Post save(Post post);

    void delete(Post post);

    Optional<Post> findById(Long id);

    boolean existsById(Long id);

    Page<Post> findByUserId(Long userId, Pageable pageable);

    Page<Post> findByGroupIdOrderByCreatedAtDesc(Long groupId, Pageable pageable);

    Page<Post> findByTagsId(Long tagId, Pageable pageable);

    int countByGroupId(Long groupId);

    int countByUserId(Long userId);

    @Query("""
                SELECT COUNT(p) FROM Post p
                WHERE p.user.id = :userId         
                AND p.group.id IS NULL
                AND p.privacy = 'PUBLIC'
            """)
    int countPublicPostsInProfile(@Param("userId") Long userId);


    int countByUserIdAndGroupId(Long userId, Long groupId);

    int countByTagsId(Long id);

    @Query("""
            SELECT COUNT(p) FROM Post p
            """)
    int countAll();

    // Friend's Posts
    @Query("""
            SELECT p FROM Post p
            WHERE p.privacy = 'PUBLIC'
            AND p.user.id IN (SELECT f.requestReceiver.id FROM Friendship f WHERE f.requester.id = :userId AND f.status = 'ACCEPTED'
                            UNION
                            SELECT f.requester.id FROM Friendship f WHERE f.requestReceiver.id = :userId AND f.status = 'ACCEPTED')
            ORDER BY p.createdAt DESC
            """)
    Page<Post> findPublicFriendPosts(@Param("userId") Long userId, Pageable pageable);

    // Posts of group user have joined, can be used for Group Activities
    @Query("""
            SELECT p FROM Post p
            WHERE p.group.id IN (SELECT g.id FROM Group g WHERE :userId IN (SELECT u.id FROM g.admins u)
                                OR :userId IN (SELECT u.id FROM g.members u))
            ORDER BY p.createdAt DESC, FUNCTION('RAND')
            """)
    Page<Post> findJoinedGroupPosts(@Param("userId") Long userId, Pageable pageable);

    // Newsfeed Posts, find by friends public posts, groups joined posts, and user's
    // posts
    @Query("""
            SELECT p FROM Post p
            WHERE p.privacy = 'PUBLIC'
            AND (p.user.id = :userId
                OR p.user.id IN (SELECT f.requestReceiver.id FROM Friendship f WHERE f.requester.id = :userId AND f.status = 'ACCEPTED'
                                 UNION
                                 SELECT f.requester.id FROM Friendship f WHERE f.requestReceiver.id = :userId AND f.status = 'ACCEPTED')
                OR p.group.id IN (SELECT g.id FROM Group g WHERE :userId IN (SELECT u.id FROM g.admins u)
                                  OR :userId IN (SELECT u.id FROM g.members u)))
            ORDER BY p.createdAt DESC
            """)
    Page<Post> findNewsfeedPosts(@Param("userId") Long userId, Pageable pageable);

    @Query("""
                SELECT p FROM Post p
                WHERE p.user.id = :userId 
                AND p.group.id IS NULL
                ORDER BY p.createdAt DESC
            """)
    Page<Post> findPostsByUserExceptGroup(@Param("userId") Long userId, Pageable pageable);

    @Query(
            value = "SELECT DATE_FORMAT(p.created_at, '%Y-%m') AS month, COUNT(*) AS count " +
                    "FROM posts p " +
                    "GROUP BY DATE_FORMAT(p.created_at, '%Y-%m')",
            nativeQuery = true
    )
    List<Object[]> countPostsPerMonth();


//    @Query(value = """
//            SELECT p.*
//            FROM posts p
//            WHERE
//                p.privacy = 'PUBLIC'
//                OR
//                p.user_id = :userId
//                OR
//                (p.privacy = 'FRIENDS' AND p.user_id IN (
//                    SELECT f.request_receiver_id
//                    FROM friendships f
//                    WHERE f.requester_id = :userId AND f.status = 'ACCEPTED'
//                    UNION
//                    SELECT f.requester_id
//                    FROM friendships f
//                    WHERE f.request_receiver_id = :userId AND f.status = 'ACCEPTED'
//                ))
//                OR
//                p.group_id IN (
//                    SELECT g.id
//                    FROM groups g
//                    WHERE :userId IN (
//                        SELECT gu.user_id
//                        FROM group_admins gu
//                        WHERE gu.group_id = g.id
//                    )
//                    OR :userId IN (
//                        SELECT gm.user_id
//                        FROM group_members gm
//                        WHERE gm.group_id = g.id
//                    )
//                )
//            ORDER BY p.created_at DESC
//            """, nativeQuery = true)
//    Page<Post> findNewsfeedPosts(@Param("userId") Long userId, Pageable pageable);


}
