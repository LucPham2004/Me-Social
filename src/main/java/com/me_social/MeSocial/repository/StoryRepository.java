package com.me_social.MeSocial.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.me_social.MeSocial.entity.modal.Story;

public interface StoryRepository extends PagingAndSortingRepository<Story, String>{
    Story save(Story story);

    void delete(Story story);

    boolean existsById(String id);

    Optional<Story> findById(String id);

    void deleteById(String id);

    Page<Story> findAllByUserId(Long id, Pageable pageable);
    
    @Query("""
            SELECT COUNT(s) FROM Story s
            """)
    int countAll();
}
