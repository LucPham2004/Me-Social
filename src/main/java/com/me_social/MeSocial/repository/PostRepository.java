package com.me_social.MeSocial.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.me_social.MeSocial.entity.modal.Post;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
    Post save(Post post);

    void delete(Post post);

    Optional<Post> findById(Long id);

    boolean existsById(Long id);

    Page<Post> findByUserId(Long userId, Pageable pageable);

    Page<Post> findByGroupIdOrderByCreatedAtDesc(Long groupId, Pageable pageable);

    Page<Post> findByTagsId(Long tagId, Pageable pageable);

    int countByGroupId(Long groupId);

    int countByUserId(Long userId);

    int countByUserIdAndGroupId(Long userId, Long groupId);

    int countByTagsId(Long id);

    // Friend's Posts
    @Query("""
        SELECT p FROM Post p 
        WHERE p.privacy = 'PUBLIC'
        AND p.isRead = false
        AND p.user.id IN (SELECT f.requestReceiver.id FROM Friendship f WHERE f.requester.id = :userId AND f.status = 'ACCEPTED'
                        UNION
                        SELECT f.requester.id FROM Friendship f WHERE f.requestReceiver.id = :userId AND f.status = 'ACCEPTED')
        ORDER BY p.createdAt DESC
        """)
    Page<Post> findUnreadPublicFriendPosts(@Param("userId") Long userId, Pageable pageable);

    // Posts of group user have joined, can be used for Group Activities
    @Query("""
        SELECT p FROM Post p
        WHERE p.privacy = 'PUBLIC'
        AND p.isRead = false
        AND p.group.id IN (SELECT g.id FROM Group g WHERE :userId IN (SELECT u.id FROM g.admins u)
                            OR :userId IN (SELECT u.id FROM g.members u))
        ORDER BY p.createdAt DESC, FUNCTION('RAND')
        """)
    Page<Post> findUnreadPublicGroupPosts(@Param("userId") Long userId, Pageable pageable);

    // Newsfeed Posts, find by friends public posts, groups joined posts, and user's posts
    @Query("""
        SELECT p FROM Post p
        WHERE p.privacy = 'PUBLIC'
        AND p.isRead = false
        AND (p.user.id = :userId
            OR p.user.id IN (SELECT f.requestReceiver.id FROM Friendship f WHERE f.requester.id = :userId AND f.status = 'ACCEPTED'
                             UNION
                             SELECT f.requester.id FROM Friendship f WHERE f.requestReceiver.id = :userId AND f.status = 'ACCEPTED')
            OR p.group.id IN (SELECT g.id FROM Group g WHERE :userId IN (SELECT u.id FROM g.admins u)
                              OR :userId IN (SELECT u.id FROM g.members u)))
        ORDER BY p.createdAt DESC
        """)
    Page<Post> findNewsfeedPosts(@Param("userId") Long userId, Pageable pageable);


}
