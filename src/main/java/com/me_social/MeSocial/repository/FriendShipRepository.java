package com.me_social.MeSocial.repository;

import java.util.Set;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.me_social.MeSocial.entity.modal.Friendship;

@Repository
public interface FriendShipRepository extends PagingAndSortingRepository<Friendship, Long> {
    Friendship save(Friendship friendShip);

    void delete(Friendship friendship);

    Friendship findById(Long id);

    boolean existsById(Long id);

    Set<Friendship> findByRequesterId(Long requesterId);

    Set<Friendship> findByRequestReceiverId(Long userId);
}
