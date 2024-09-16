package com.me_social.MeSocial.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.me_social.MeSocial.entity.modal.DirectMessage;

@Repository
public interface DirectMessageRepository extends PagingAndSortingRepository<DirectMessage, Long> {
    
}
