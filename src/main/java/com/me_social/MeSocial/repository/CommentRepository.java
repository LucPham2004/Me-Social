package com.me_social.MeSocial.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.me_social.MeSocial.entity.modal.Comment;

@Repository
public interface CommentRepository extends PagingAndSortingRepository<Comment, Long> {
    Comment save(Comment comment);

    void delete(Comment comment);

    Optional<Comment> findById(Long id);

    boolean existsById(Long id);

    Page<Comment> findByPostId(Long id, Pageable pageable);

    Page<Comment> findByUserId(Long id, Pageable pageable);

    int countByPostId(Long id);

    int countByUserId(Long id);
}
