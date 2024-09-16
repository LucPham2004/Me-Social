package com.me_social.MeSocial.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.me_social.MeSocial.entity.modal.Follow;
import com.me_social.MeSocial.entity.modal.User;

public interface FollowRepository extends PagingAndSortingRepository<Follow, Long> {
    Page<Follow> findByFollower(User follower, Pageable pageable);

    Page<Follow> findByFollowing(User following, Pageable pageable);
}
