package com.me_social.MeSocial.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.me_social.MeSocial.entity.modal.DirectMessage;

@Repository
public interface DirectMessageRepository extends PagingAndSortingRepository<DirectMessage, Long> {
    DirectMessage save(DirectMessage directMessage);

    void delete(DirectMessage directMessage);

    DirectMessage findById(Long id);

    boolean existsById(Long id);

    Page<DirectMessage> findBySenderIdAndReceiverId(Long senderId, Long receiverId, Pageable pageable);

    int countBySenderIdAndReceiverId(Long senderId, Long receiverId);

}
