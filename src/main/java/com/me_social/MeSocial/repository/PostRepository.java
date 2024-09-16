package com.me_social.MeSocial.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.me_social.MeSocial.entity.modal.Post;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
    Post save(Post post);

    void delete(Post post);

    Post findById(Long id);

    boolean existsById(Long id);

    Page<Post> findAllByUserId(Long id, Pageable pageable);

    Page<Post> findAllByGroupId(Long id, Pageable pageable);

}
