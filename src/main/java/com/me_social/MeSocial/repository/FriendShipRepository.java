package com.me_social.MeSocial.repository;


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

    Friendship findById(Long id);

    boolean existsById(Long id);

    @Query("SELECT f FROM Friendship f " +
           "WHERE (f.requester.id = :userId OR f.requestReceiver.id = :userId) " +
           "AND f.status = 'ACCEPTED'")
    Page<Friendship> findByRequesterIdOrRequestReceiverId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT COUNT(f) FROM Friendship f " +
           "WHERE (f.requester.id = :userId OR f.requestReceiver.id = :userId) " +
           "AND f.status = 'ACCEPTED'")
    int countAcceptedFriendshipsByUserId(@Param("userId") Long userId);
}
