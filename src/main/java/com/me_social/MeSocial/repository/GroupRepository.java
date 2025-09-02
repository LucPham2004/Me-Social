package com.me_social.MeSocial.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.me_social.MeSocial.entity.modal.Group;
import com.me_social.MeSocial.entity.modal.User;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Group save(Group group);

    void delete(Group group);
    
    Optional<Group> findById(Long id);

    boolean existsById(Long id);
    
    Page<Group> findByMembersIdOrAdminsId(Long adminId, Long memberId, Pageable pageable);
    
    @Query("""
            SELECT COUNT(g) FROM Group g
            """)
    int countAll();

    // Suggest groups sort by member count and post count, mix in each page
    @Query("""
        SELECT g FROM Group g
        LEFT JOIN g.members m
        LEFT JOIN g.admins a
        LEFT JOIN g.posts p
        WHERE g.id NOT IN (
            SELECT g1.id FROM Group g1 
            JOIN g1.members m1 
            WHERE m1.id = :userId
            UNION
            SELECT g2.id FROM Group g2 
            JOIN g2.admins a2 
            WHERE a2.id = :userId
        )
        GROUP BY g.id
        ORDER BY COUNT(m) DESC, COUNT(p) DESC, FUNCTION('RAND')
        """)
    Page<Group> findSuggestedGroups(@Param("userId") Long userId, Pageable pageable);

    @Query("""
            SELECT a FROM Group g 
            JOIN g.admins a 
            WHERE g.id = :id
            """)
    Page<User> findAdminsById(@Param("id") Long id, Pageable pageable);

    @Query("""
            SELECT m FROM Group g 
            JOIN g.members m 
            WHERE g.id = :id
            """)
    Page<User> findMembersById(@Param("id") Long id, Pageable pageable);

    @Query("""
            SELECT COUNT(g) FROM Group g 
            JOIN g.admins a 
            JOIN g.members m 
            WHERE a.id = :userId OR m.id = :userId
            """)
    int countGroupsByUserId(@Param("userId") Long userId);

    @Query("""
            SELECT COUNT(m) FROM Group g 
            JOIN g.members m 
            WHERE g.id = :id
            """)
    int countMembersById(@Param("id") Long id);

    @Query("""
            SELECT COUNT(a) FROM Group g 
            JOIN g.admins a 
            WHERE g.id = :id
            """)
    int countAdminsById(@Param("id") Long id);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM groups_members WHERE group_id = :groupId AND user_id = :userId)",
            nativeQuery = true)
    int existsByUserIdInGroup(@Param("userId") Long userId, @Param("groupId") Long groupId);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM groups_admins WHERE group_id = :groupId AND user_id = :userId)",
            nativeQuery = true)
    int existsByAdminIdInGroup(@Param("userId") Long userId, @Param("groupId") Long groupId);

}
