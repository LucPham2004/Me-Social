package com.me_social.MeSocial.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.me_social.MeSocial.entity.modal.Group;

@Repository
public interface GroupRepository extends PagingAndSortingRepository<Group, Long> {
    Group save(Group group);
    
    Group findById(Long id);

    boolean existsById(Long id);
    
    Page<Group> findByMembersIdOrAdminsId(Long adminId, Long memberId, Pageable pageable);
}
