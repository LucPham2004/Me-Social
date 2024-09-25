package com.me_social.MeSocial.repository;

import java.util.Set;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.me_social.MeSocial.entity.modal.Group;

@Repository
public interface GroupRepository extends PagingAndSortingRepository<Group, Long> {
    Group save(Group group);

    void delete(Group group);
    
    Group findById(Long id);

    boolean existsById(Long id);
    
    Set<Group> findByMembersIdOrAdminsId(Long adminId, Long memberId);
}
