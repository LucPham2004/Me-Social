package com.me_social.MeSocial.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.me_social.MeSocial.entity.modal.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Tag findByName(String name);

    @Override
    Page<Tag> findAll(Pageable pageable);

    Page<Tag> findAllOrderByPostDesc(Pageable pageable);
}
