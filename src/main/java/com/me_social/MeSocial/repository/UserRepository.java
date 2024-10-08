package com.me_social.MeSocial.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
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

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String username);

    Optional<User> findByPhone(String phoneNumber);

    void deleteById(Long id);

    // User's friends
    @Query("""
           SELECT u FROM User u 
           JOIN Friendship f 
           ON (f.requester = u OR f.requestReceiver = u)
           WHERE (f.requester.id = :userId OR f.requestReceiver.id = :userId)
           AND f.status = 'ACCEPTED'
           AND u.id != :userId
           """)
    Page<User> findFriends(@Param("userId") Long userId, Pageable pageable);

    @Query("""
           SELECT COUNT(u) FROM User u 
           JOIN Friendship f 
           ON (f.requester = u OR f.requestReceiver = u)
           WHERE (f.requester.id = :userId OR f.requestReceiver.id = :userId)
           AND f.status = 'ACCEPTED'
           AND u.id != :userId
           """)
    int countFriends(@Param("userId") Long userId);

    // 2 User's mutual friends
    @Query("SELECT u FROM User u WHERE u.id IN " +
           "(SELECT f.requestReceiver.id FROM Friendship f " +
           "WHERE f.requester.id = :userAId AND f.requestReceiver.id IN " +
           "(SELECT f2.requestReceiver.id FROM Friendship f2 WHERE f2.requester.id = :userBId)) " +
           "OR u.id IN " +
           "(SELECT f3.requester.id FROM Friendship f3 " +
           "WHERE f3.requestReceiver.id = :userAId AND f3.requester.id IN " +
           "(SELECT f4.requester.id FROM Friendship f4 WHERE f4.requestReceiver.id = :userBId))")
    Page<User> findMutualFriends(@Param("userAId") Long userAId, @Param("userBId") Long userBId, Pageable pageable);

    @Query("SELECT COUNT(f) FROM Friendship f " +
           "WHERE (f.requester.id = :userAId AND f.requestReceiver.id IN " +
           "   (SELECT f2.requestReceiver.id FROM Friendship f2 WHERE f2.requester.id = :userBId)) " +
           "OR (f.requestReceiver.id = :userAId AND f.requester.id IN " +
           "   (SELECT f3.requester.id FROM Friendship f3 WHERE f3.requestReceiver.id = :userBId))")
    int countMutualFriends(@Param("userAId") Long userAId, @Param("userBId") Long userBId);

    @Query("SELECT u.id, COUNT(f) FROM Friendship f " +
       "JOIN User u ON (f.requester.id = :userId AND f.requestReceiver.id IN :memberIds " +
       "OR f.requestReceiver.id = :userId AND f.requester.id IN :memberIds) " +
       "GROUP BY u.id")
    Map<Long, Long> countMutualFriendsForUsers(@Param("userId") Long userId, @Param("memberIds") List<Long> memberIds);


}
