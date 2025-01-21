package com.me_social.MeSocial.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.me_social.MeSocial.entity.modal.Friendship;

@Repository
public interface FriendShipRepository extends PagingAndSortingRepository<Friendship, Long> {
    Friendship save(Friendship friendShip);

    void delete(Friendship friendship);

    Optional<Friendship> findById(Long id);

    boolean existsById(Long id);

    @Query("""
            SELECT f FROM  Friendship f 
            WHERE ((f.requester.id = :requesterId AND f.requestReceiver.id = :receiverId) OR
                 (f.requester.id = :receiverId AND f.requestReceiver.id = :requesterId))
            """)
    Friendship findBy2UserIds(Long requesterId, Long receiverId);

    @Query("""
            SELECT COUNT(f) FROM Friendship f
            """)
    int countAll();

    @Query("""
               SELECT f FROM Friendship f
               WHERE f.requestReceiver.id = :userId AND f.status = 'PENDING'        
            """)
    Page<Friendship> findFriendRequestByUser(Long userId, Pageable pageable);

    @Query("""
                   SELECT f from Friendship f
                   WHERE (f.requester.id = :userId OR f.requestReceiver.id = :userId)
                   AND f.status = 'ACCEPTED' 
            """)
    Page<Friendship> findUserFriends(Long userId, Pageable pageable);

    @Query("""
                SELECT f FROM Friendship f
                WHERE (f.requester.id = :userId OR f.requestReceiver.id = :userId)
                AND f.status = 'ACCEPTED'
                AND f.acceptedAt >= :oneHourAgo
            """)
    List<Friendship> findRecentCreatedFriendshipsByUserId(@Param("userId") Long userId,
                                                          @Param("oneHourAgo") Instant oneHourAgo);

}
