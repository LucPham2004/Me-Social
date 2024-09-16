package com.me_social.MeSocial.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.me_social.MeSocial.entity.modal.User;


@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    User save(User user);

    void delete(User user);

    User findById(Long id);

    boolean existsByUsername(String username);

    boolean existsById(Long id);

}
