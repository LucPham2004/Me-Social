package com.me_social.MeSocial.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.me_social.MeSocial.entity.modal.Comment;

@Repository
public interface CommentRepository extends PagingAndSortingRepository<Comment, Long> {
    Comment save(Comment comment);

    void delete(Comment comment);

    Comment findById(Long id);

    boolean existsById(Long id);
}
