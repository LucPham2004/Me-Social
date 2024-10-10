package com.me_social.MeSocial.service;

import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.me_social.MeSocial.entity.modal.Story;
import com.me_social.MeSocial.entity.modal.User;
import com.me_social.MeSocial.exception.AppException;
import com.me_social.MeSocial.exception.ErrorCode;
import com.me_social.MeSocial.repository.StoryRepository;
import com.me_social.MeSocial.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StoryService {
    StoryRepository storyRepository;
    UserRepository userRepository;
    UserService userService;
    
    static int STORIES_PER_PAGE = 10;

    // Create story
    public Story createStory(Long userId, MultipartFile file, String content) {
        User user = userService.findById(userId)
            .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_EXISTED));

        Story story = new Story();
        // More handling for the file can be done here
        story.setUser(user);
        story.setCreatedAt(Instant.now());

        return storyRepository.save(story);
    }

    // Get by id
    public Story getStoryById(String id) {
        return storyRepository.findById(id)
            .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_EXISTED));
    }

    // Get all stories paginated
    public Page<Story> getStoriesByUserId(Long userId, int pageNum) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, STORIES_PER_PAGE);
        return storyRepository.findAllByUserId(userId, pageable);
    }

    // Delete story
    public void deleteStory(String id) {
        if (!storyRepository.existsById(id)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        storyRepository.deleteById(id);
    }
}
