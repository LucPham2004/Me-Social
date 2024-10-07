package com.me_social.MeSocial.service;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.services.youtube.model.Video;
import com.me_social.MeSocial.entity.dto.response.ApiResponse;
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
    YouTubeService youTubeService;
    
    static int STORIES_PER_PAGE = 10;

    // Create story
    public ApiResponse<Story> createStory(Long userId, MultipartFile file, String content) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        Story story = new Story();
        try {
            story.setId(youTubeService.uploadVideo(file, user.getFirstName(), content));
        } catch (IOException ex) {
            System.out.println(Arrays.toString(ex.getStackTrace()));
        }
        story.setUser(user);
        story.setCreatedAt(Instant.now());

        ApiResponse<Story> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Created story successfully");
        apiResponse.setResult(storyRepository.save(story));

        return apiResponse;
    }

    // Get by id
    public ApiResponse<Video> GetStoryById(String id) {
        Story story = storyRepository.findById(id);
        if (story == null) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        ApiResponse<Video> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Get story successfully");
        try {
            apiResponse.setResult(youTubeService.getVideoById(id));
        } catch (IOException ex) {
            System.out.println(Arrays.toString(ex.getStackTrace()));
        }

        return apiResponse;
    }

    //  Get all stories paginated
    public ApiResponse<Page<Story>> GetStorysByUserId(Long userId, int pageNum) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, STORIES_PER_PAGE);

        ApiResponse<Page<Story>> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Get storys by user successfully");
        apiResponse.setResult(storyRepository.findAllByUserId(userId, pageable));

        return apiResponse;
    }

    public ApiResponse<String> deleteStory(String id) {
        if (storyRepository.existsById(id)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        storyRepository.deleteById(id);
        ApiResponse<String> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Delete story successfully");
        apiResponse.setResult("");

        return apiResponse;
    }
}
