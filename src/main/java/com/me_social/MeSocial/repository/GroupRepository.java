package com.me_social.MeSocial.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.me_social.MeSocial.entity.modal.Group;

@Repository
public interface GroupRepository extends PagingAndSortingRepository<Group, Long> {
    Group save(Group group);

    void delete(Group group);
    
    Optional<Group> findById(Long id);

    boolean existsById(Long id);
    
    Set<Group> findByMembersIdOrAdminsId(Long adminId, Long memberId);

    @Query("SELECT COUNT(g) FROM Group g " +
       "JOIN g.admins a " +
       "JOIN g.members m " +
       "WHERE a.id = :userId OR m.id = :userId")
    int countGroupsByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(m) FROM Group g " +
           "JOIN g.members m " +
           "WHERE g.id = :id")
    int countMembersById(@Param("id") Long id);

    @Query("SELECT COUNT(a) FROM Group g " +
           "JOIN g.admins a " +
           "WHERE g.id = :id")
    int countAdminsById(@Param("id") Long id);
}
