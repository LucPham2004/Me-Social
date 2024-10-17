package com.me_social.MeSocial.service;

import org.springframework.stereotype.Service;

import com.me_social.MeSocial.entity.dto.response.DashboardSummary;
import com.me_social.MeSocial.repository.DashboardRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DashboardService {

    DashboardRepository repository;
    
    public DashboardSummary getDashboardSummary() {
        DashboardSummary summary = new DashboardSummary();

        summary.setTotalComments(repository.countAllComments());
        summary.setTotalGroups(repository.countAllGroups());
        summary.setTotalLikes(repository.countAllLikes());
        summary.setTotalPosts(repository.countAllPosts());
        summary.setTotalReels(repository.countAllReels());
        summary.setTotalStories(repository.countAllStories());
        summary.setTotalUsers(repository.countAllUsers());
        
        return summary;
    }
}
