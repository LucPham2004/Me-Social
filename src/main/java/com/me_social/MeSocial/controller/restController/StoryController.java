package com.me_social.MeSocial.controller.restController;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.me_social.MeSocial.entity.dto.request.StoryRequest;
import com.me_social.MeSocial.entity.dto.response.ApiResponse;
import com.me_social.MeSocial.entity.dto.response.StoryResponse;
import com.me_social.MeSocial.entity.modal.Story;
import com.me_social.MeSocial.mapper.StoryMapper;
import com.me_social.MeSocial.service.StoryService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/stories")
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
public class StoryController {
    StoryService storyService;
    StoryMapper storyMapper;

    // Create story
    @PostMapping
    public ApiResponse<StoryResponse> createStory(@RequestBody StoryRequest request) {
        Story story = storyService.createStory(request);
        return ApiResponse.<StoryResponse>builder()
            .code(1000)
            .message("Created story successfully")
            .result(storyMapper.toResponse(story))
            .build();
    }

    // Get story by id
    @GetMapping("/{id}")
    public ApiResponse<StoryResponse> getStoryById(@PathVariable String id) {
        Story story = storyService.getStoryById(id);
        return ApiResponse.<StoryResponse>builder()
            .code(1000)
            .message("Get story successfully")
            .result(storyMapper.toResponse(story))
            .build();
    }

    // Get all stories by user
    @GetMapping("/user")
    public ApiResponse<Page<StoryResponse>> getStoriesByUserId(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int pageNum) {
        Page<Story> stories = storyService.getStoriesByUserId(userId, pageNum);
        return ApiResponse.<Page<StoryResponse>>builder()
            .code(1000)
            .message("Get stories by user successfully")
            .result(stories.map(storyMapper::toResponse))
            .build();
    }

    // Delete story
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteStory(@PathVariable String id) {
        storyService.deleteStory(id);
        return ApiResponse.<Void>builder()
            .code(1000)
            .message("Delete story successfully")
            .build();
    }
}
