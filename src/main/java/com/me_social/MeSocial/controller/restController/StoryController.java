package com.me_social.MeSocial.controller.restController;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.me_social.MeSocial.entity.dto.response.ApiResponse;
import com.me_social.MeSocial.entity.modal.Story;
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

    @PostMapping("/upload-video")
    public ApiResponse<Story> createStory(@RequestParam("file") MultipartFile file,
                                @RequestParam Long userId,
                                @RequestParam String content) {
        return storyService.createStory(userId, file, content);
    }

    @GetMapping("/{id}")
    public ApiResponse<String> getVStoryById(@PathVariable String id) {
        return storyService.GetStoryById(id);
    }

    @GetMapping("/user")
    public ApiResponse<Page<Story>> GetStorysByUserId(
        @RequestParam Long userId, 
        @RequestParam(defaultValue = "0") int pageNum) {
        return storyService.GetStorysByUserId(userId, pageNum);
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<String> deleteStory(@PathVariable String id) {
        return storyService.deleteStory(id);
    }
}
