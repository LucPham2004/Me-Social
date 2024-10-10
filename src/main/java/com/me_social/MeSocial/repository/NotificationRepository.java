package com.me_social.MeSocial.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.me_social.MeSocial.entity.modal.Notification;

@Repository
public interface NotificationRepository extends PagingAndSortingRepository<Notification, Long> {
    Notification save(Notification notification);

    void delete(Notification notification);

    void deleteById(Long id);
    
    Notification findById(Long id);

    boolean existsById(Long id);

    Page<Notification> findByUserId(Long userId, Pageable pageable);
}
