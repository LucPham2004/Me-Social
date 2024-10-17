package com.me_social.MeSocial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.me_social.MeSocial.entity.modal.User;

@Repository
public interface DashboardRepository extends JpaRepository<User, Long>{
    @Query("""
            SELECT COUNT(c) FROM Comment c
            """)
    int countAllComments();
    
    @Query("""
            SELECT COUNT(g) FROM Group g
            """)
    int countAllGroups();
    
    @Query("""
            SELECT COUNT(l) FROM Like l
            """)
    int countAllLikes();
    
    @Query("""
        SELECT COUNT(p) FROM Post p
        """)
    int countAllPosts();

    @Query("""
        SELECT COUNT(u) FROM User u
        """)
    int countAllUsers();
    
    @Query("""
            SELECT COUNT(r) FROM Reel r
            """)
    int countAllReels();

    @Query("""
            SELECT COUNT(s) FROM Story s
            """)
    int countAllStories();
}
