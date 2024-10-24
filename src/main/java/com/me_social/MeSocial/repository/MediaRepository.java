package com.me_social.MeSocial.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.me_social.MeSocial.entity.modal.Media;

@Repository
public interface MediaRepository extends PagingAndSortingRepository<Media, String> {
    Media save(Media media);

    void delete(Media media);

    Optional<Media> findById(String id);

    boolean existsById(String id);

    Page<Media> findByPostId(Long postId, Pageable pageable);

    int countByPostId(Long id);
}
