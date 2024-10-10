package com.me_social.MeSocial.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
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

}
