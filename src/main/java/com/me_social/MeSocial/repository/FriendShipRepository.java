package com.me_social.MeSocial.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.me_social.MeSocial.entity.modal.Friendship;
import com.me_social.MeSocial.entity.modal.User;

@Repository
public interface FriendShipRepository extends PagingAndSortingRepository<Friendship, Long> {
    Page<Friendship> findByRequester(User requester, Pageable pageable);

    Page<Friendship> findByAccepter(User accepter, Pageable pageable);
}
