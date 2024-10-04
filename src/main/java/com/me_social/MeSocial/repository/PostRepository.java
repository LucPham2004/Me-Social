package com.me_social.MeSocial.repository;

import java.util.Set;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.me_social.MeSocial.entity.modal.Post;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
    Post save(Post post);

    void delete(Post post);

    Post findById(Long id);

    boolean existsById(Long id);

    Set<Post> findByUserId(Long id);

    Set<Post> findByGroupId(Long id);

    Set<Post> findByTagsId(Long id);

    int countByGroupId(Long id);

    int countByUserId(Long id);

    int countByUserIdAndGroupId(Long userId, Long groupId);

    int countByTagsId(Long id);
}
