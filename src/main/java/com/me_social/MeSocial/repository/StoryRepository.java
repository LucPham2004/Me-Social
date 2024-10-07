package com.me_social.MeSocial.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.me_social.MeSocial.entity.modal.Story;

public interface StoryRepository extends PagingAndSortingRepository<Story, String>{
    Story save(Story story);

    void delete(Story story);

    boolean existsById(String id);

    Story findById(String id);

    void deleteById(String id);

    Page<Story> findAllByUserId(Long id, Pageable pageable);
}
