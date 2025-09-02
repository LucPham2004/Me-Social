package com.me_social.MeSocial.entity.dto.response;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class StatisticDTO {
    private long totalUsers;
    private long totalComments;
    private long totalLikes;
    private long totalGroups;
    private long totalPosts;

    private Map<String, Long> totalUsersPerMonth;
    private Map<String, Long> totalPostsPerMonth;
    private Map<String, Long> totalLikesPerMonth;
    private Map<String, Long> totalCommentsPerMonth;
}