package com.me_social.MeSocial.service;

import com.me_social.MeSocial.entity.dto.response.StatisticDTO;
import com.me_social.MeSocial.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticService {
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final GroupRepository groupRepository;
    private final PostRepository postRepository;

    public StatisticDTO getStatistics() {
        long totalUsers = userRepository.countAll();
        long totalComments = commentRepository.countAll();
        long totalLikes = likeRepository.count();
        long totalGroups = groupRepository.count();
        long totalPosts = postRepository.count();

        List<Object[]> results = userRepository.countUsersPerMonth();
        List<Object[]> postResults = postRepository.countPostsPerMonth();
        List<Object[]> likeResults = likeRepository.countLikesPerMonth();
        List<Object[]> commentResults = commentRepository.countCommentsPerMonth();

        Map<String, Long> totalUsersPerMonth = new HashMap<>();
        Map<String, Long> totalPostsPerMonth = new HashMap<>();
        Map<String, Long> totalLikesPerMonth = new HashMap<>();
        Map<String, Long> totalCommentsPerMonth = new HashMap<>();

        for (Object[] row : results) {
            String month = (String) row[0];
            Long count = ((Number) row[1]).longValue();
            totalUsersPerMonth.put(month, count);
        }
        for (Object[] row : postResults) {
            String month = (String) row[0];
            Long count = ((Number) row[1]).longValue();
            totalPostsPerMonth.put(month, count);
        }
        for (Object[] row : likeResults) {
            String month = (String) row[0];
            Long count = ((Number) row[1]).longValue();
            totalLikesPerMonth.put(month, count);
        }
        for (Object[] row : commentResults) {
            String month = (String) row[0];
            Long count = ((Number) row[1]).longValue();
            totalCommentsPerMonth.put(month, count);
        }

        return new StatisticDTO(totalUsers, totalComments, totalLikes, totalGroups, totalPosts, totalUsersPerMonth, totalPostsPerMonth, totalLikesPerMonth, totalCommentsPerMonth);
    }
}
