package com.me_social.MeSocial.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.me_social.MeSocial.entity.modal.Reel;

public interface ReelRepository extends PagingAndSortingRepository<Reel, String>{
    Reel save(Reel Reel);

    void delete(Reel Reel);

    boolean existsById(String id);

    Optional<Reel> findById(String id);

    void deleteById(String id);

    Page<Reel> findAllByUserId(Long id, Pageable pageable);

    @Query("""
            SELECT COUNT(re) FROM Reel re
            """)
    int countAll();
}
