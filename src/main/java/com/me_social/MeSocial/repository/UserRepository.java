package com.me_social.MeSocial.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.me_social.MeSocial.entity.modal.User;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    User save(User user);

    void delete(User user);

    boolean existsByUsername(String username);

    boolean existsById(Long id);

    boolean existsByEmail(String email);

    User findById(long id);

    Object findByUsername(String username);

    User findByEmail(String username);

}
