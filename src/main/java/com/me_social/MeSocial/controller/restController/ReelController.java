package com.me_social.MeSocial.controller.restController;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.services.youtube.model.Video;
import com.me_social.MeSocial.entity.dto.response.ApiResponse;
import com.me_social.MeSocial.entity.modal.Reel;
import com.me_social.MeSocial.service.ReelService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/reels")
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
public class ReelController {
    
    ReelService reelService;

    @PostMapping("/upload-video")
    public ApiResponse<Reel> uploadVideo(@RequestParam("file") MultipartFile file,
                                @RequestParam Long userId,
                                @RequestParam String content) {
        return reelService.createReel(userId, file, content);
    }

    @GetMapping("/{id}")
    public ApiResponse<Video> getVideoById(@PathVariable String id) {
        return reelService.GetReelById(id);
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<Page<Reel>> GetReelsByUserId(@RequestParam Long userId, int pageNum) {
        return reelService.GetReelsByUserId(userId, pageNum);
    }
}

