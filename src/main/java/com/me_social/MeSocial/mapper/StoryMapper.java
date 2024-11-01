package com.me_social.MeSocial.mapper;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.me_social.MeSocial.entity.dto.request.StoryRequest;
import com.me_social.MeSocial.entity.dto.response.StoryResponse;
import com.me_social.MeSocial.entity.modal.Media;
import com.me_social.MeSocial.entity.modal.Story;
import com.me_social.MeSocial.repository.LikeRepository;
import com.me_social.MeSocial.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal = true)
public class StoryMapper {
    LikeRepository likeRepository;
    UserRepository userRepository;

    public Story toStory(StoryRequest request) {
        Story story = new Story();
        story.setId(request.getPublicId());
        story.setUser(userRepository.findById(request.getUserId()).get());
        story.setUrl(request.getUrl());

        return story;
    }
    
    public StoryResponse toResponse(Story story) {
        StoryResponse response = new StoryResponse();

        response.setId(story.getId());
        response.setUserId(story.getUser().getId());
        response.setCreatedAt(story.getCreatedAt());
        response.setUpdatedAt(story.getUpdatedAt());
        response.setLikeNum(likeRepository.countByStoryId(story.getId()));

        Set<Media> medias = story.getMedias();
        if (medias != null && !medias.isEmpty()) {
            String[] publicIds = medias.stream()
                .map(Media::getPublicId)
                .toArray(String[]::new);

            String[] urls = medias.stream()
                .map(Media::getUrl)
                .toArray(String[]::new);

            response.setPublicIds(publicIds);
            response.setUrls(urls);
        }

        return response;
    }
}
